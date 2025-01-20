package neh.memocards.domain.entities.estudio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import neh.memocards.domain.entities.usuarios.Usuario;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AllArgsConstructor
@Getter
public class Estudiante {
    // Atributos
    @Setter
    private String nombre;
    @Setter
    private String apellido;
    @Setter
    private Usuario usuario;
    private Set<TematicaEstudio> tematicas;
    private List<Configurador> preferencias;
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
    public void iniciarSesionDeEstudio(Long idTematica, Long idMazo) {

        Mazo mazoAEstudiar = buscarTematicaPorId(idTematica).buscarMazoPorId(idMazo);

        SesionDeEstudio nuevaSesion = new SesionDeEstudio(mazoAEstudiar.memoCardsPorRevisar(), this, mazoAEstudiar);

        nuevaSesion.comenzarSesion();

        historialDeEstudio.add(nuevaSesion);

    }


}
