package neh.memocards;

import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.Aprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.EstadoMemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.Reaprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.Repaso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static java.sql.DriverManager.println;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;


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

        intervalo = estado.calcularIntervalo(intervalo, 0);
        assertEquals(5067L, intervalo);
        assertInstanceOf(Repaso.class, estado.getMemoCard().getEstadoAprendizaje());

    }


    @Test
    void testIntervalosDeReaprendizajeTarjetasNuevas() {
        memoCard.setEstadoAprendizaje(new Reaprendizaje(memoCard));
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
        assertTrue(estado instanceof Reaprendizaje);

        Long intervalo = estado.calcularIntervalo(memoCard.getEstadoAprendizaje().getIntervaloActual(), 1);
        assertEquals(1440L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals(4320L, intervalo);

        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals(10800L, intervalo);
    }

    @Test
    void testRecalcularIntervalosSegunRespuestas() {
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();

        // "Otra vez"
        Long intervalo = estado.calcularIntervalo(1440L, 3);
        assertEquals(15L, intervalo);

        // "Difícil"
        intervalo = estado.calcularIntervalo(intervalo, 2);
        assertEquals((long) (1440 * 0.8333), intervalo);

        // "Bien"
        intervalo = estado.calcularIntervalo(intervalo, 1);
        assertEquals((long) (4320 * 0.8333), intervalo);

        // "Fácil"
        intervalo = estado.calcularIntervalo(intervalo, 0);
        assertEquals((long) (4320 * 0.8333 * 1.3), intervalo);
    }

    @Test
    void testProgresionDeEstadosDeTarjetas() {
        // Pasar de "Aprendizaje" a "Repaso"
        EstadoMemoCard estado = memoCard.getEstadoAprendizaje();
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
        // Olvidar la tarjeta más de 8 veces
        for (int i = 0; i < 8; i++) {
            memoCard.setCantidadDeOlvidos(memoCard.getCantidadDeOlvidos() + 1);
        }

        assertFalse(memoCard.getEsSanguijuela());

        memoCard.setCantidadDeOlvidos(memoCard.getCantidadDeOlvidos() + 1);
        assertTrue(memoCard.getEsSanguijuela());
    }
}
