package neh.memocards.domain.entities.estudio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import neh.memocards.domain.entities.usuarios.Usuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
@Entity
@Table(name = "estudiantes")
public class Estudiante {
    // Atributos
    @Setter
    @Id @GeneratedValue
    private Long id;

    @Setter
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Setter
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Setter
    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "estudiante")
    private Set<TematicaEstudio> tematicas;

    @OneToMany(mappedBy = "estudiante")
    private List<Configurador> preferencias;

    @OneToMany(mappedBy = "estudiante")
    private List<SesionDeEstudio> historialDeEstudio;

    // Metodos
    public Estudiante() {
        tematicas = new HashSet<>();
        preferencias = new ArrayList<>();
        historialDeEstudio = new ArrayList<>();
    }

    //----Gestion-de-TematicaDeEstudio----
    public void agregarTematicaDeEstudio(TematicaEstudio tematica) {
        if (laTematicaEsExistente(tematica)) {
            throw new RuntimeException("No se puede agregar una Tematica ya existente");
        }
        this.tematicas.add(tematica);
        tematica.setEstudiante(this);
    }

    private boolean laTematicaEsExistente(TematicaEstudio tematica) {
        return this.buscarTematicaPorId(tematica.getId()) != null || this.buscarTematicaPorNombre(tematica.getNombre()) != null;
    }

    private TematicaEstudio buscarTematicaPorId(Long id) {
        return this.tematicas.stream().filter(tematica -> tematica.getId().equals(id)).findFirst().orElse(null);
    }

    private TematicaEstudio buscarTematicaPorNombre(String nombreBuscado) {
        return this.tematicas.stream().filter(tematica -> tematica.getNombre().equals(nombreBuscado)).findFirst().orElse(null);
    }

    public void eleminarTematica(TematicaEstudio tematica) {
        this.tematicas.remove(tematica);
    }

    //----Gestion-de-Configurador----
    public void agregarConfiguracion(Configurador configuracion) {
        this.preferencias.add(configuracion);
    }

    public void agregarConfigurador(Configurador configurador) {
        preferencias.add(configurador);
    }

    //----Gestion-de-Mazos----
    public void agregarMazoATematica(Mazo mazo, Long idTematica) {
        TematicaEstudio tematica = buscarTematicaPorId(idTematica);
        if (tematica == null) {
            throw new RuntimeException("No se puede agregar un Mazo sin una Tematica asociada");
        }
        if (tematica.elMazoEsExistente(mazo)) {
            throw new RuntimeException("No se puede agregar un Mazo si existe uno con mismo Id o Nombre");
        }
        tematica.agregarMazo(mazo);
    }

    public void eliminarMazo(Mazo mazo, Long idTematica) {
        this.buscarTematicaPorId(idTematica).agregarMazo(mazo);
    }

    //----Gestion-de-MemoCards----
    public void agregarMemoCard(MemoCard memoCard, Long idTematica, Long idMazo) {
        TematicaEstudio tematica = buscarTematicaPorId(idTematica);
        if (tematica == null) {
            throw new RuntimeException("No se puede agregar una MemoCard sin una Tematica asociada");
        }
        Mazo mazoElegido = tematica.buscarMazoPorId(idMazo);
        if (mazoElegido == null) {
            throw new RuntimeException("No se puede agregar una MemoCard sin un Mazo asociada");
        }
        if (mazoElegido.laMemoCardEsExistente(memoCard)) {
            throw new RuntimeException("No se puede agregar una MemoCard repetida en el mismo Mazo");
        }
        mazoElegido.agregarMemoCard(memoCard);
    }

    public void eliminarMemoCard(MemoCard memoCard, Long idMazo, Long idTematica) {
        this.buscarTematicaPorId(idTematica).buscarMazoPorId(idMazo).eliminarMemoCard(memoCard);
    }

    //----Gestion-de-SesionDeEstudio----
    public SesionDeEstudio iniciarSesionDeEstudio(Long idTematica, Long idMazo) {

        Mazo mazoAEstudiar = buscarTematicaPorId(idTematica).buscarMazoPorId(idMazo);

        SesionDeEstudio nuevaSesion = new SesionDeEstudio(mazoAEstudiar.generarBarajaDeMemoCards(), this, mazoAEstudiar);

        nuevaSesion.comenzarSesion();

        historialDeEstudio.add(nuevaSesion);

        return nuevaSesion;
    }


}
