package neh.memocards;

import neh.memocards.domain.entities.estudio.*;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import neh.memocards.domain.entities.estudio.memocard.RespuestaMemo;
import neh.memocards.domain.entities.estudio.memocard.estados.Aprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.Reaprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.Repaso;
import org.junit.jupiter.api.*;
import utils.CircularList;

import static neh.memocards.domain.entities.estudio.memocard.Dificultad.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class SesionDeEstudioTest {

    private Mazo mazo;
    private SesionDeEstudio sesion;
    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        Configurador configurador = Configurador.configuradorPredeterminado(); // 2 nuevas, 5 repasadas, 24 horas mínimo
        configurador.setMaximoDeNuevasCartas(2);
        configurador.setMaximoDeCartasARepasar(5);
        mazo = new Mazo();
        mazo.setId(1L);
        mazo.setNombre("Mazo de Prueba");
        mazo.setPreferencia(configurador);

        // Agregar 15 cartas de prueba
        for (int i = 1; i <= 15; i++) {
            var memoCard = new MemoCard();
            memoCard.setId((long)i);
            memoCard.setNombre("Memo card " + i);
            memoCard.setFechaUltimoRepaso(LocalDateTime.now().minusMinutes(2000));
            memoCard.setRespuesta(new RespuestaMemo("Respuesta " + i));
            memoCard.setConfigurador(configurador);
            memoCard.setEstadoMemoCard(new Aprendizaje(memoCard));

            if (i <= 5) {
                mazo.agregarMemoCard(memoCard);
            } else {
                memoCard.setIntentos(2);
                mazo.getMemoCardsVistas().add(memoCard);
            }
        }

        estudiante = new Estudiante();
        estudiante.setNombre("Estudiante");
        TematicaEstudio tematica = new TematicaEstudio();
        tematica.setId(1L);
        estudiante.agregarTematicaDeEstudio(tematica);
        estudiante.agregarMazoATematica(mazo, 1L);

        sesion = new SesionDeEstudio(mazo.generarBarajaDeMemoCards(), estudiante, mazo);
        // estudiante.iniciarSesionDeEstudio(1L,1L);
    }

    @Test
    @DisplayName("6.1 - Permitir iniciar y cerrar una sesión de estudio")
    void testIniciarYCerrarSesion() {
        assertFalse(sesion.estaActiva(), "La sesión debe estar inactiva inicialmente");
        sesion.comenzarSesion();
        assertTrue(sesion.estaActiva(), "La sesión debe estar activa tras iniciar");
        sesion.finalizarSesion();
        assertFalse(sesion.estaActiva(), "La sesión debe estar inactiva tras cerrar");
    }

    @Test
    @DisplayName("6.2 - Actualizar cartas repasadas y pendientes dinámicamente en cada sesión")
    void testActualizarCartasRepasadasYPendientes() {
        sesion.comenzarSesion();
        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();
        assertEquals(7, activas.size(), "Debe haber 7 cartas activas inicialmente (2 nuevas y 5 repasadas)");
        //verificamos que siempre luego del primer feedback en cartas con intervalos bajos estas se mantienen en Aprendizaje o Reaprendizaje
        sesion.estudiarMemoCard(activas.next(), BIEN);
        sesion.estudiarMemoCard(activas.next(), OLVIDO);
        assertInstanceOf(Aprendizaje.class, activas.get(0).getEstadoMemoCard(), "La carta marcada como BIEN debe pasar seguir en aprendizaje");
        assertInstanceOf(Aprendizaje.class, activas.get(1).getEstadoMemoCard(), "La carta marcada como OLVIDO debe seguir en aprendizaje");
        for(int i = 2; i <= 6 ; i++ ){
            sesion.estudiarMemoCard(activas.next(), OLVIDO);
        }

        long idMemo = activas.get(0).getId();
        assertEquals(0, activas.getElements().stream().filter(memo-> memo.getEstadoMemoCard() instanceof Repaso).toList().size(), "La carta marcada como OLVIDO debe seguir en aprendizaje");

        //cuando una memoCard supera el umbral maximo de intervalo por sesion esta sale de la lista activa y al mazo como vista
        sesion.estudiarMemoCard(activas.next(), BIEN);
        assertFalse(activas.getElements().stream().anyMatch(memo -> memo.getId() == idMemo), "La carta marcada como BIEN no debe estar como activa");
        assertTrue(mazo.getMemoCardsVistas().stream().anyMatch(memo -> memo.getId() == idMemo));

        sesion.finalizarSesion();
    }

    @Test
    @DisplayName("6.3 - Incluir tarjetas nuevas en la lista activa solo bajo condiciones específicas")
    void testIncluirTarjetasNuevas() {
        // Condición inicial: No se han incluido tarjetas nuevas en las últimas 24 horas
        mazo.setFechaUltimaInclusionNuevasMemoCards(LocalDateTime.now().minusHours(25));
        sesion.comenzarSesion();

        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();
        assertEquals(7, activas.size(), "Debe haber 7 cartas activas inicialmente (2 nuevas y 5 repasadas)");

        // Verificar que las cartas nuevas incluidas cumplen las restricciones
        long nuevas = activas.getElements().stream().filter(c -> c.getIntentos() == 0).count();
        assertEquals(2, nuevas, "Debe haber 2 cartas nuevas en la sesión activa");
    }

    @Test
    @DisplayName("6.4 - Mantener tarjetas pendientes al cerrar sesión sin interacción")
    void testMantenerCartasPendientes() {
        sesion.comenzarSesion();
        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();
        sesion.finalizarSesion();

        // Las cartas pendientes (sin interacción) deben seguir en la lista activa
        assertEquals(7, activas.size(), "Las cartas pendientes deben mantenerse al cerrar la sesión sin interacción");
    }

    @Test
    @DisplayName("7.1 - Restringir la cantidad máxima de cartas nuevas por sesión")
    void testRestriccionCartasNuevasPorSesion() {
        mazo.setFechaUltimaInclusionNuevasMemoCards(LocalDateTime.now().minusHours(25)); // Garantizar inclusión de nuevas cartas
        sesion.comenzarSesion();

        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();
        long nuevas = activas.getElements().stream().filter(c -> c.getEstadoMemoCard() instanceof Aprendizaje).count();
        assertTrue(nuevas <= 2, "No debe haber más de 2 cartas nuevas por sesión");
    }

    @Test
    @DisplayName("7.2 - Restringir la cantidad máxima de cartas repasadas activas por sesión")
    void testRestriccionCartasRepasadasPorSesion() {
        sesion.comenzarSesion();
        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();

        long repasadas = activas.getElements().stream().filter(c -> c.getEstadoMemoCard() instanceof Repaso).count();
        assertTrue(repasadas <= 5, "No debe haber más de 5 cartas repasadas activas por sesión");
    }

    @Test
    @DisplayName("7.3 - Evitar inclusión automática de cartas nuevas sin sesión de estudio")
    void testEvitarInclusionAutomaticaCartasNuevas() {
        // Intentar incluir nuevas cartas sin iniciar sesión
        mazo.setFechaUltimaInclusionNuevasMemoCards(LocalDateTime.now().minusHours(25)); // Condición válida para nuevas cartas
        CircularList<MemoCard> activas = mazo.getMemoCardsEnRepasoActivo();

        assertEquals(0, activas.getElements().stream().filter(c -> c.getEstadoMemoCard() instanceof Aprendizaje).count(),
                "Las cartas nuevas no deben incluirse automáticamente sin una sesión de estudio");
    }

    @Test
    @DisplayName("8.1 - Registrar intervalos de repaso incluso sin iniciar sesión")
    void testRegistrarIntervalosSinSesion() {
        MemoCard carta = mazo.getMemoCardsVistas().stream().toList().get(0);
        LocalDateTime proximoRepaso = carta.fechaProximoRepaso();

        // Simular tiempo transcurrido
        carta.getEstadoMemoCard().cambiarIntervalo(carta.getIntervaloMinutos(), FACIL);
        assertTrue(carta.fechaProximoRepaso().isAfter(proximoRepaso),
                "El intervalo de repaso debe registrarse y actualizarse incluso sin iniciar sesión");
    }

    @Test
    @DisplayName("8.3 - Mantener cartas 'Olvido' en la lista activa hasta ser repasadas correctamente")
    void testMantenerCartasOlvidoEnListaActiva() {
        sesion.comenzarSesion();
        CircularList<MemoCard> activas = sesion.getMazo().getMemoCardsEnRepasoActivo();
        MemoCard olvidada = activas.next();

        // Marcar como OLVIDO
        sesion.estudiarMemoCard(olvidada, OLVIDO);
        sesion.finalizarSesion();

        // Verificar que la carta sigue en la lista activa
        assertTrue(sesion.getMazo().getMemoCardsEnRepasoActivo().getElements().contains(olvidada),
                "Las cartas marcadas como 'Olvido' deben permanecer en la lista activa");
    }

}
