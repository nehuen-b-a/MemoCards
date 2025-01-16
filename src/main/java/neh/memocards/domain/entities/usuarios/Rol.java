package neh.memocards.domain.entities.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class Rol {

    private Long id;

    @Setter
    private TipoRol tipoRol;

    private Set<Permiso> permisos;

    public Rol(TipoRol tipo) {
        this.tipoRol = tipo;
        this.permisos = new HashSet<>();
    }

    public boolean tenesPermiso(Permiso permiso) {
        return permisos.contains(permiso);
    }

    public void agregarPermiso(Permiso permiso) {
        permisos.add(permiso);
    }

    public void quitarPermiso(Permiso permiso) {
        permisos.remove(permiso);
    }
}
