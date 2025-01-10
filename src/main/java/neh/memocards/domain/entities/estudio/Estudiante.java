package neh.memocards.domain.entities.estudio;

import neh.memocards.domain.entities.usuarios.Usuario;

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
