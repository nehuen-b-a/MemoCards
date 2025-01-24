package neh.memocards.domain.entities.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(name = "permiso")
@NoArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="nombre")
    private String nombre;

    public Permiso(String nombre) {
        this.nombre = nombre;
    }
}
