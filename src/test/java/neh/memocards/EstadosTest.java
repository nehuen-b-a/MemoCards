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
import java.time.LocalDateTime;
import java.util.Arrays;


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
        assertTrue(estado instanceof Aprendizaje);

        Long intervalo = estado.calcularIntervalo(0L, 2);
        assertEquals(15L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals(1200L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals(3600L, intervalo);
    }

    @Test
    void testRecalcularIntervalosSegunRespuestas() {
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();

        // "Otra vez"
        Long intervalo = estado.calcularIntervalo(1440L, 1);
        assertEquals(15L, intervalo);
        assertEquals(0.975, memoCard.getConfigurador().getFactorFacilidad());

        // "Difícil"
        intervalo = estado.calcularIntervalo(intervalo, 2);
        assertEquals((long) (15 * 1.2), intervalo);
        assertEquals(1.105, memoCard.getConfigurador().getFactorFacilidad());

        // "Bien"
        intervalo = estado.calcularIntervalo(intervalo, 3);
        assertEquals((long) (15 * 1.105), intervalo);
        assertEquals(1.105, memoCard.getConfigurador().getFactorFacilidad());

        // "Fácil"
        intervalo = estado.calcularIntervalo(intervalo, 4);
        assertEquals((long) (15 * 1.105 * 1.3), intervalo);
        assertEquals(1.271, memoCard.getConfigurador().getFactorFacilidad());
    }

    @Test
    void testProgresionDeEstadosDeTarjetas() {
        // Pasar de "Aprendizaje" a "Repaso"
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        estado.calcularIntervalo(0L, 3);
        estado.calcularIntervalo(15L, 3);
        estado.calcularIntervalo(1440L, 3);

        assertTrue(memoCard.getEstadoAprendizaje() instanceof Repaso);

        // Regresar a "Reaprendizaje" desde "Repaso"
        estado = memoCard.getEstadoAprendizaje();
        estado.calcularIntervalo(4320L, 1);

        assertTrue(memoCard.getEstadoAprendizaje() instanceof Reaprendizaje);
    }

    @Test
    void testUmbralDeSanguijuelas() {
        // Olvidar la tarjeta más de 8 veces
        for (int i = 0; i < 8; i++) {
            memoCard.setCantidadDeOlvidos(memoCard.getCantidadDeOlvidos() + 1);
        }

        assertFalse(memoCard.getEsSanguijuela());

        memoCard.setCantidadDeOlvidos(memoCard.getCantidadDeOlvidos() + 1);
        assertTrue(memoCard.getEsSanguijuela());
    }
}
