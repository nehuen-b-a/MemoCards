package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class TematicaEstudio {
    // Atributos
    @Setter
    private Long id;
    @Setter
    private String nombre;
    @Setter
    private String descripcion;
    @Getter
    private Set<Mazo> mazos;

    // Métodos (por implementar según necesidades)


    public TematicaEstudio() {
        this.mazos = new HashSet<>();
    }


    public void agregarMazo(Mazo mazo){
        this.mazos.add(mazo);
    }

    public Mazo buscarMazoPorId(Long id){
        return this.mazos.stream().filter(mazo -> mazo.getId().equals(id)).findFirst().orElse(null);
    }

    public Mazo buscarMazoPorNombre(String nombreBuscado){
        return this.mazos.stream().filter(mazo -> mazo.getNombre().equals(nombreBuscado)).findFirst().orElse(null);
    }

    public Boolean elMazoEsExistente (Mazo mazo){
        return this.buscarMazoPorId(mazo.getId()) != null  || this.buscarMazoPorNombre(mazo.getNombre()) != null;
    }
}