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
        memoCard.setEstadoAprendizaje(new Aprendizaje(memoCard));
        memoCard.setCantidadDeOlvidos(0);
        memoCard.setEsSanguijuela(false);
    }

    @Test
    void testIntervalosInicialesTarjetasNuevas() {
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        assertInstanceOf(Aprendizaje.class, estado.getMemoCard().getEstadoAprendizaje());

        Long intervalo = estado.calcularIntervalo(memoCard.getEstadoAprendizaje().getIntervaloActual(), 2);
        assertEquals(15L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 2);
        assertEquals(999L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 0);
        assertEquals(3898L, intervalo);

        estado.calcularIntervalo(intervalo, 0);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoAprendizaje());

        intervalo = memoCard.getEstadoAprendizaje().getIntervaloActual();
        assertEquals(12668L, intervalo);

    }


    @Test
    void testIntervalosDeReaprendizajeTarjetas() {
        memoCard.setEstadoAprendizaje(new Reaprendizaje(memoCard));

        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        assertTrue(estado instanceof Reaprendizaje);

        Long intervalo = estado.calcularIntervalo(memoCard.getEstadoAprendizaje().getIntervaloActual(), 1);
        assertEquals(1440L, intervalo);

        intervalo = estado.calcularIntervalo(memoCard.getEstadoAprendizaje().getIntervaloActual(), 1);
        assertEquals(4320L, intervalo);

        estado.calcularIntervalo(memoCard.getEstadoAprendizaje().getIntervaloActual(), 1);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoAprendizaje());

        //cambio de estado
        intervalo = memoCard.getEstadoAprendizaje().getIntervaloActual();
        assertEquals(10800L, intervalo);
    }

    @Test
    void testRecalcularIntervalosSegunRespuestas() {
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        estado.setIntentos(5);
        // "Otra vez"
        Long intervalo = estado.calcularIntervalo(1440L, 3);
        assertEquals(15L, intervalo);

        // "Difícil"
        intervalo = estado.calcularIntervalo(intervalo, 2);
        assertEquals(1199L, intervalo);

        // "Bien"
        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals(3599L, intervalo);

        // "Fácil"
        estado.calcularIntervalo(intervalo, 0);
        //cambio de estado
        intervalo = memoCard.getEstadoAprendizaje().getIntervaloActual();

        assertEquals(11696L, intervalo);
    }

    @Test
    void testProgresionDeEstadosDeTarjetas() {
        // Pasar de "Aprendizaje" a "Repaso"
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        estado.setIntentos(5);
        estado.calcularIntervalo(estado.getIntervaloActual(), 0);
        estado.calcularIntervalo(estado.getIntervaloActual(), 0);
        estado.calcularIntervalo(estado.getIntervaloActual(), 0);
        estado.calcularIntervalo(estado.getIntervaloActual(), 0);

        assertTrue(memoCard.getEstadoAprendizaje() instanceof Repaso);

        // Regresar a "Reaprendizaje" desde "Repaso"
        estado = memoCard.getEstadoAprendizaje();
        estado.calcularIntervalo(4320L, 3);

        assertTrue(memoCard.getEstadoAprendizaje() instanceof Reaprendizaje);
    }

    @Test
    void testUmbralDeSanguijuelas() {
        // Olvidar la tarjeta más de 7 veces

        for (int i = 0; i < 7; i++) {
            memoCard.
                    getEstadoAprendizaje().
                    calcularIntervalo(memoCard.getEstadoAprendizaje().
                            getIntervaloActual(), 3);
        }

        assertFalse(memoCard.getEsSanguijuela());

        // Olvidar la tarjeta más de 8 veces
        memoCard.
                getEstadoAprendizaje().
                calcularIntervalo(memoCard.getEstadoAprendizaje().
                        getIntervaloActual(), 3);

        assertTrue(memoCard.getEsSanguijuela());
    }

    @Test
    void testIntervalosMaximosYMinimos() {
        // Configurar valores extremos en el configurador
        memoCard.getConfigurador().setIntervaloMaximo(20000L);
        memoCard.getConfigurador().setIntervaloMinimo(10L);
        memoCard.getEstadoAprendizaje().actualizarConfiguracion();

        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();

        // Probar un intervalo por debajo del mínimo
        Long intervalo = estado.calcularIntervalo(5L, 2);
        assertEquals(10L, intervalo, "El intervalo debería ajustarse al mínimo permitido.");

        // Paso a Repaso
        memoCard.getEstadoAprendizaje().actualizarEstado();

        // Probar un intervalo por encima del máximo
        intervalo = estado.calcularIntervalo(25000L, 0);
        assertEquals(20000L, intervalo, "El intervalo debería ajustarse al máximo permitido.");
    }

}
