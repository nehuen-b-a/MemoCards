package neh.memocards;

import neh.memocards.domain.entities.estudio.Estudiante;
import neh.memocards.domain.entities.estudio.Mazo;
import neh.memocards.domain.entities.estudio.TematicaEstudio;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GestionDeMazosTest {

    private Estudiante estudiante;
    private TematicaEstudio tematica;
    private Mazo mazo;


    @BeforeEach
    void setUp() {
        // Crear mazo
        estudiante = new Estudiante();
        tematica = new TematicaEstudio();
        tematica.setId(1L);
        tematica.setNombre("Matemáticas");
        tematica.setDescripcion("Temática relacionada con matemáticas");

        mazo = new Mazo();
        mazo.setId(1L);
        mazo.setNombre("Álgebra");
        mazo.setDescripcion("Mazo sobre álgebra");

        estudiante.agregarTematicaDeEstudio(tematica);
        estudiante.agregarMazoATematica(mazo, tematica.getId());

        assertTrue(tematica.getMazos().contains(mazo), "El mazo no se agregó correctamente");
    }

    @Test
    void testCrearLeerActualizarEliminarMazo() {
        // Leer mazo
        Mazo mazoLeido = tematica.buscarMazoPorId(mazo.getId());
        assertNotNull(mazoLeido, "El mazo no fue encontrado");
        assertEquals("Álgebra", mazoLeido.getNombre(), "El nombre del mazo no coincide");

        // Actualizar mazo
        mazoLeido.setDescripcion("Nuevo contenido sobre álgebra");
        assertEquals("Nuevo contenido sobre álgebra", mazoLeido.getDescripcion(), "La descripción del mazo no se actualizó correctamente");

        // Eliminar mazo
        tematica.getMazos().remove(mazo);
        assertFalse(tematica.getMazos().contains(mazo), "El mazo no se eliminó correctamente");
    }

    @Test
    void testAsociarTarjetasAMazo() {
        // Crear y asociar mazo


        // Crear tarjeta
        MemoCard tarjeta = new MemoCard();
        tarjeta.setId(1L);
        tarjeta.setNombre("Ecuaciones lineales");
        mazo.agregarMemoCard(tarjeta);

        // Verificar asociación
        assertTrue(mazo.getMemoCardsNoVistas().contains(tarjeta), "La tarjeta no se agregó al mazo correctamente");
    }

    @Test
    void testCrearLeerActualizarEliminarTarjetaDentroDeUnMazo() {
        // Crear y asociar mazo


        // Crear tarjeta
        MemoCard tarjeta = new MemoCard();
        tarjeta.setId(1L);
        tarjeta.setNombre("Ecuaciones lineales");
        mazo.agregarMemoCard(tarjeta);

        // Leer tarjeta
        MemoCard tarjetaLeida = mazo.getMemoCardsNoVistas().stream().filter(t -> t.getId().equals(tarjeta.getId())).findFirst().orElse(null);
        assertNotNull(tarjetaLeida, "La tarjeta no fue encontrada");

        // Actualizar tarjeta
        tarjetaLeida.setNombre("Ecuaciones cuadráticas");
        assertEquals("Ecuaciones cuadráticas", tarjetaLeida.getNombre(), "El nombre de la tarjeta no se actualizó correctamente");

        // Eliminar tarjeta
        mazo.getMemoCardsNoVistas().remove(tarjetaLeida);
        assertFalse(mazo.getMemoCardsNoVistas().contains(tarjetaLeida), "La tarjeta no se eliminó correctamente");
    }

    @Test
    void testEvitarDuplicadosEnNombresDeMazosYContenidoDeTarjetas() {
        // Crear y asociar mazo

            // Intentar agregar un mazo con el mismo nombre
            Mazo mazoDuplicado = new Mazo();
            mazoDuplicado.setId(2L);
            mazoDuplicado.setNombre("Álgebra");

            Exception exception = assertThrows(RuntimeException.class, () -> {
                estudiante.agregarMazoATematica(mazoDuplicado, tematica.getId());
            });

        assertEquals("No se puede agregar un Mazo si existe uno con mismo Id o Nombre", exception.getMessage());

        // Crear tarjeta
        MemoCard tarjeta = new MemoCard();
        tarjeta.setId(1L);
        tarjeta.setNombre("Ecuaciones lineales");
        mazo.agregarMemoCard(tarjeta);

        // Intentar agregar tarjeta duplicada
        MemoCard tarjetaDuplicada = new MemoCard();
        tarjetaDuplicada.setId(2L);
        tarjetaDuplicada.setNombre("Ecuaciones lineales");

        Exception exceptionTarjeta = assertThrows(RuntimeException.class, () -> {
            estudiante.agregarMemoCard(tarjetaDuplicada, tematica.getId(), mazo.getId());
        });
        assertEquals("No se puede agregar una MemoCard repetida en el mismo Mazo", exceptionTarjeta.getMessage());
    }
}
