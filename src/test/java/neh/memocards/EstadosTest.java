package neh.memocards;

import neh.memocards.domain.entities.estudio.*;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.Aprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.EstadoMemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.Reaprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.Repaso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstadosTest {

    private MemoCard memoCard;

    @BeforeEach
    void setUp() {
        Configurador configurador = Configurador.configuradorPredeterminado();

        var mazo = new Mazo();
        mazo.setId(1L);
        mazo.setNombre("Mazo de Prueba");
        mazo.setPreferencia(configurador);

        memoCard = new MemoCard();
        memoCard.setConfigurador(configurador);
        memoCard.setEstadoMemoCard(new Aprendizaje(memoCard));
        memoCard.setCantidadDeOlvidos(0);
        memoCard.setEsSanguijuela(false);

        mazo.agregarMemoCard(memoCard);

        var estudiante = new Estudiante();
        estudiante.setNombre("Estudiante");
        TematicaEstudio tematica = new TematicaEstudio();
        tematica.setId(1L);
        estudiante.agregarTematicaDeEstudio(tematica);
        estudiante.agregarMazoATematica(mazo, 1L);

        estudiante.iniciarSesionDeEstudio(1L,1L);

    }

    @Test
    void testIntervalosInicialesTarjetasNuevas() {
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();
        assertInstanceOf(Aprendizaje.class, estado.getMemoCard().getEstadoMemoCard());

        Long intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), Dificultad.DIFICIL);
        assertEquals(15L, intervalo);

        intervalo = estado.cambiarIntervalo(intervalo, Dificultad.DIFICIL);
        assertEquals(999L, intervalo);

        intervalo = estado.cambiarIntervalo(intervalo, Dificultad.FACIL);
        assertEquals(3898L, intervalo);

        estado.cambiarIntervalo(intervalo, Dificultad.FACIL);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoMemoCard());

        intervalo = memoCard.getEstadoMemoCard().getIntervaloActual();
        assertEquals(12668L, intervalo);

    }


    @Test
    void testIntervalosDeReaprendizajeTarjetas() {
        memoCard.setEstadoMemoCard(new Reaprendizaje(memoCard, 9000L, 0, 5));
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        assertInstanceOf(Reaprendizaje.class, estado);

        Long intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), Dificultad.BIEN);

        assertEquals(1440L, intervalo);

        intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), Dificultad.BIEN);

        assertEquals(4320L, intervalo);

        estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), Dificultad.BIEN);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoMemoCard());

        //cambio de estado
        intervalo = memoCard.getEstadoMemoCard().getIntervaloActual();
        assertEquals(10800L, intervalo);
    }

    @Test
    void testRecalcularIntervalosSegunRespuestas() {
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();
        memoCard.setIntentos(5);
        // "Otra vez"
        Long intervalo = estado.cambiarIntervalo(1440L, Dificultad.OLVIDO);
        assertEquals(15L, intervalo);

        // "Difícil"
        intervalo = estado.cambiarIntervalo(intervalo, Dificultad.DIFICIL);
        assertEquals(1199L, intervalo);

        // "Bien"
        intervalo = estado.cambiarIntervalo(intervalo, Dificultad.BIEN);
        assertEquals(3599L, intervalo);

        // "Fácil"
        estado.cambiarIntervalo(intervalo, Dificultad.FACIL);
        //cambio de estado
        intervalo = memoCard.getEstadoMemoCard().getIntervaloActual();

        assertEquals(11696L, intervalo);
    }

    @Test
    void testProgresionDeEstadosDeTarjetas() {
        // Pasar de "Aprendizaje" a "Repaso"
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();
        memoCard.setIntentos(5);
        estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.FACIL);
        estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.FACIL);
        estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.FACIL);
        estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.FACIL);

        assertInstanceOf(Repaso.class, memoCard.getEstadoMemoCard());

        // Regresar a "Reaprendizaje" desde "Repaso"
        estado = memoCard.getEstadoMemoCard();
        estado.cambiarIntervalo(4320L, Dificultad.OLVIDO);

        assertInstanceOf(Reaprendizaje.class, memoCard.getEstadoMemoCard());
    }

    @Test
    void testUmbralDeSanguijuelas() {
        // Olvidar la tarjeta más de 7 veces

        for (int i = 0; i < 7; i++) {
            memoCard.
                    getEstadoMemoCard().
                    cambiarIntervalo(memoCard.getEstadoMemoCard().
                            getIntervaloActual(), Dificultad.OLVIDO);
        }

        assertFalse(memoCard.getEsSanguijuela());

        // Olvidar la tarjeta más de 8 veces
        memoCard.
                getEstadoMemoCard().
                cambiarIntervalo(memoCard.getEstadoMemoCard().
                        getIntervaloActual(), Dificultad.OLVIDO);

        assertTrue(memoCard.getEsSanguijuela());
    }

    @Test
    void testIntervalosMaximosYMinimos() {
        // Configurar valores extremos en el configurador
        memoCard.getConfigurador().setIntervaloMaximo(20000L);
        memoCard.getEstadoMemoCard().actualizarEstado(); // pasamos a Repaso
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        // Probar un intervalo por encima del máximo
        Long intervalo = estado.cambiarIntervalo(25000L, Dificultad.FACIL);
        assertEquals(20000L, intervalo, "El intervalo debería ajustarse al máximo permitido.");

        estado.cambiarIntervalo(25000L, Dificultad.OLVIDO);//pasamos a Reaprendizaje

        memoCard.getConfigurador().setIntervaloMinimo(500L);//cambio el intervalo minimo para probar la actualizacion de configuracion
        memoCard.getEstadoMemoCard().actualizarConfiguracion();

        // Probar un intervalo por debajo del mínimo
        memoCard.getEstadoMemoCard().cambiarIntervalo(5L, Dificultad.DIFICIL);
        assertEquals(416L, ((Reaprendizaje) memoCard.getEstadoMemoCard()).getIntervalosBonificados().get(0), "El intervalo debería ajustarse al mínimo permitido.");

    }

    @Test
    void testMultiplesDesaciertos() {
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        // Acumulamos errores consecutivos
        for (int i = 0; i < 5; i++) {
            estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.OLVIDO);
        }

        // Comprobamos que el sistema se recalibra correctamente
        assertEquals(15L, estado.getIntervaloActual(), "El intervalo debería recalibrarse al mínimo tras varios errores.");
        assertInstanceOf(Aprendizaje.class, estado, "Debería permanecer en el estado Aprendizaje.");
    }


    @Test
    void testCicloDeVidaCompletoDeMemoCard() {
        // 1. Creamos un configurador
        Configurador configurador = Configurador.configuradorPredeterminado();

        // 2. Creamos una MemoCard
        memoCard.setConfigurador(configurador);
        memoCard.setEstadoMemoCard(new Aprendizaje(memoCard));
        memoCard.setCantidadDeOlvidos(0);
        memoCard.setEsSanguijuela(false);

        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        // 3. Estimamos intervalos iniciales sin cambiar estado
        Long intervaloEstimado = estado.estimarIntervalo(estado.getIntervaloActual(), Dificultad.DIFICIL);
        Long intervaloCambiado = estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.DIFICIL);
        assertEquals(intervaloEstimado, intervaloCambiado, "La estimación y el cambio deben ser iguales.");
        assertEquals(15L, intervaloEstimado);

        // 4. Se elige una dificultad: Difícil (Dificultad.DIFICIL)
        intervaloCambiado = estado.cambiarIntervalo(intervaloCambiado, Dificultad.DIFICIL);
        assertEquals(999L, intervaloCambiado);

        // 5. Se estiman intervalos después de elegir Difícil
        intervaloEstimado = estado.estimarIntervalo(intervaloCambiado, Dificultad.DIFICIL);
        assertEquals(intervaloEstimado, intervaloCambiado);

        // 6. Se elige Bien (Dificultad.BIEN)
        intervaloCambiado = estado.cambiarIntervalo(intervaloCambiado, Dificultad.BIEN);
        assertEquals(2999L, intervaloCambiado);

        // 7. Se estiman intervalos después de elegir Bien
        intervaloEstimado = estado.estimarIntervalo(intervaloCambiado, Dificultad.BIEN);
        assertEquals(intervaloEstimado, intervaloCambiado);

        // 8. Se elige Fácil y se cambia a Repaso (Dificultad.FACIL)
        intervaloCambiado = estado.cambiarIntervalo(intervaloCambiado, Dificultad.FACIL);
        estado = memoCard.getEstadoMemoCard();
        assertInstanceOf(Repaso.class, estado, "La tarjeta debería pasar al estado Repaso.");
        assertEquals(9746L, intervaloCambiado);

        // 9. Se estiman intervalos después de elegir Dificil
        Long intervaloAnterior = intervaloCambiado;
        intervaloEstimado = estado.estimarIntervalo(intervaloCambiado, Dificultad.DIFICIL);
        intervaloCambiado = estado.cambiarIntervalo(intervaloAnterior, Dificultad.DIFICIL);
        assertEquals(intervaloEstimado, intervaloCambiado);

        // 10. Validación del cálculo en Repaso: intervaloAnterior * bonificación * 2.5
        Long intervaloEsperado = Math.round(intervaloAnterior * configurador.getBonusDificil() * configurador.getCoeficienteDeRetencion());
        assertEquals(intervaloEsperado, intervaloCambiado);

        // 11. La tarjeta debería haberse mantenido estado "Repaso" apesar de varios intentos acertados
        estado.cambiarIntervalo(intervaloCambiado, Dificultad.FACIL); // Cambio a Repaso
        assertInstanceOf(Repaso.class, memoCard.getEstadoMemoCard(), "La tarjeta debería haberse mantenido estado Repaso.");

        // 12. Cambio de estado a "Reaprendizaje" tras un Olvido (Dificultad.OLVIDO)
        estado.cambiarIntervalo(intervaloCambiado, Dificultad.OLVIDO);
        assertInstanceOf(Reaprendizaje.class, memoCard.getEstadoMemoCard(), "La tarjeta debería regresar al estado Reaprendizaje.");

        // 13. Validación del intervalo en Reaprendizaje
        estado = memoCard.getEstadoMemoCard();
        intervaloEstimado = estado.estimarIntervalo(estado.getIntervaloActual(), Dificultad.BIEN);
        intervaloCambiado = estado.cambiarIntervalo(estado.getIntervaloActual(), Dificultad.BIEN);
        assertEquals(intervaloEstimado, intervaloCambiado);
        assertEquals(1440L, intervaloCambiado);

        // 14. Progresión dentro de Reaprendizaje
        intervaloCambiado = estado.cambiarIntervalo(intervaloCambiado, Dificultad.BIEN);
        assertEquals(4320L, intervaloCambiado);

        // 15. Transición de Reaprendizaje a Repaso
        estado.cambiarIntervalo(intervaloCambiado, Dificultad.BIEN);
        assertInstanceOf(Repaso.class, memoCard.getEstadoMemoCard(), "La tarjeta debería pasar al estado Repaso.");
    }

}
