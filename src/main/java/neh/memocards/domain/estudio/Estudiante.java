package neh.memocards.domain.estudio;

import domain.entities.usuario.Usuario;

import java.util.Set;

public class Estudiante {
    // Atributos
    private String nombre;
    private Usuario usuario;
    private String apellido;
    private Set<TematicaEstudio> tematicas;
    private Configurador preferencias;

    // Métodos (por implementar según necesidades)
}
