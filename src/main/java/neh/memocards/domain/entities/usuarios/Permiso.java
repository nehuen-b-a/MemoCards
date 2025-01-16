package neh.memocards.domain.entities.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Permiso {

    private Long id;
    private String nombre;

    public Permiso(String nombre) {
        this.nombre = nombre;
    }
}
