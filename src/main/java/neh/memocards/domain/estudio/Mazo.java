package neh.memocards.domain.estudio;

import domain.entities.estudio.memocard.MemoCard;

import java.util.List;
import java.util.Set;

public class Mazo {
    // Atributos
    private Long id;
    private String nombre;
    private String descripcion;
    private Set<MemoCard> tarjetas;
    private Set<MemoCard> tarjetasVistas;
    private List<SesionDeEstudio> historialDeEstudio;

    // Métodos
    public void iniciarSesionDeEstudio() {
        // Implementación pendiente
    }
}
