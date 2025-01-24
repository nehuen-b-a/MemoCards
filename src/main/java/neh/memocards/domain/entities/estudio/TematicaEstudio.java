package neh.memocards.domain.entities.estudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity @Table(name = "tematicas")
public class TematicaEstudio {
    // Atributos
    @Setter
    @Id @GeneratedValue
    private Long id;

    @Setter
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Setter
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Setter
    @ManyToOne @JoinColumn(name="estudiante_id", referencedColumnName = "id")
    private Estudiante estudiante;

    @OneToMany(mappedBy = "mazos")
    private Set<Mazo> mazos;

    // Métodos

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