package neh.memocards;

import neh.memocards.domain.entities.estudio.Configurador;
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
        memoCard = new MemoCard();
        memoCard.setConfigurador(configurador);
        memoCard.setEstadoMemoCard(new Aprendizaje(memoCard));
        memoCard.setCantidadDeOlvidos(0);
        memoCard.setEsSanguijuela(false);
    }

    @Test
    void testIntervalosInicialesTarjetasNuevas() {
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();
        assertInstanceOf(Aprendizaje.class, estado.getMemoCard().getEstadoMemoCard());

        Long intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), 2);
        assertEquals(15L, intervalo);

        intervalo = estado.cambiarIntervalo(intervalo, 2);
        assertEquals(999L, intervalo);

        intervalo = estado.cambiarIntervalo(intervalo, 0);
        assertEquals(3898L, intervalo);

        estado.cambiarIntervalo(intervalo, 0);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoMemoCard());

        intervalo = memoCard.getEstadoMemoCard().getIntervaloActual();
        assertEquals(12668L, intervalo);

    }


    @Test
    void testIntervalosDeReaprendizajeTarjetas() {
        memoCard.setEstadoMemoCard(new Reaprendizaje(memoCard, 9000L, 0, 5));
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        assertInstanceOf(Reaprendizaje.class, estado);

        Long intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), 1);

        assertEquals(1440L, intervalo);

        intervalo = estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), 1);

        assertEquals(4320L, intervalo);

        estado.cambiarIntervalo(memoCard.getEstadoMemoCard().getIntervaloActual(), 1);
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
        Long intervalo = estado.cambiarIntervalo(1440L, 3);
        assertEquals(15L, intervalo);

        // "Difícil"
        intervalo = estado.cambiarIntervalo(intervalo, 2);
        assertEquals(1199L, intervalo);

        // "Bien"
        intervalo = estado.cambiarIntervalo(intervalo, 1);
        assertEquals(3599L, intervalo);

        // "Fácil"
        estado.cambiarIntervalo(intervalo, 0);
        //cambio de estado
        intervalo = memoCard.getEstadoMemoCard().getIntervaloActual();

        assertEquals(11696L, intervalo);
    }

    @Test
    void testProgresionDeEstadosDeTarjetas() {
        // Pasar de "Aprendizaje" a "Repaso"
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();
        memoCard.setIntentos(5);
        estado.cambiarIntervalo(estado.getIntervaloActual(), 0);
        estado.cambiarIntervalo(estado.getIntervaloActual(), 0);
        estado.cambiarIntervalo(estado.getIntervaloActual(), 0);
        estado.cambiarIntervalo(estado.getIntervaloActual(), 0);

        assertTrue(memoCard.getEstadoMemoCard() instanceof Repaso);

        // Regresar a "Reaprendizaje" desde "Repaso"
        estado = memoCard.getEstadoMemoCard();
        estado.cambiarIntervalo(4320L, 3);

        assertTrue(memoCard.getEstadoMemoCard() instanceof Reaprendizaje);
    }

    @Test
    void testUmbralDeSanguijuelas() {
        // Olvidar la tarjeta más de 7 veces

        for (int i = 0; i < 7; i++) {
            memoCard.
                    getEstadoMemoCard().
                    cambiarIntervalo(memoCard.getEstadoMemoCard().
                            getIntervaloActual(), 3);
        }

        assertFalse(memoCard.getEsSanguijuela());

        // Olvidar la tarjeta más de 8 veces
        memoCard.
                getEstadoMemoCard().
                cambiarIntervalo(memoCard.getEstadoMemoCard().
                        getIntervaloActual(), 3);

        assertTrue(memoCard.getEsSanguijuela());
    }

    @Test
    void testIntervalosMaximosYMinimos() {
        // Configurar valores extremos en el configurador
        memoCard.getConfigurador().setIntervaloMaximo(20000L);
        memoCard.getEstadoMemoCard().actualizarEstado(); // pasamos a Repaso
        EstadoMemoCard estado = memoCard.getEstadoMemoCard();

        // Probar un intervalo por encima del máximo
        Long intervalo = estado.cambiarIntervalo(25000L, 0);
        assertEquals(20000L, intervalo, "El intervalo debería ajustarse al máximo permitido.");

        estado.cambiarIntervalo(25000L, 3);//pasamos a Reaprendizaje

        memoCard.getConfigurador().setIntervaloMinimo(500L);//cambio el intervalo minimo para probar la actualizacion de configuracion
        memoCard.getEstadoMemoCard().actualizarConfiguracion();

        // Probar un intervalo por debajo del mínimo
        memoCard.getEstadoMemoCard().cambiarIntervalo(5L, 2);
        assertEquals(416L, ((Reaprendizaje) memoCard.getEstadoMemoCard()).getIntervalosBonificados().get(0), "El intervalo debería ajustarse al mínimo permitido.");

    }

}
