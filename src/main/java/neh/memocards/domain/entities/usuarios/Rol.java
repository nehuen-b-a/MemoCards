package neh.memocards.domain.entities.usuarios;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name ="rol")
@NoArgsConstructor
public class Rol {
    @Id @GeneratedValue
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name="tipoRol", nullable = false)
    private TipoRol tipoRol;

    @ManyToMany
    @JoinTable(
            name = "rol_permiso",
            joinColumns = @JoinColumn(name = "rol_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id", referencedColumnName = "id")
    )
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
