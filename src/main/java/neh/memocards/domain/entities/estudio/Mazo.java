package neh.memocards.domain.entities.estudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity
@Table(name="mazos")
public class Mazo {
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
    @ManyToOne
    @JoinColumn(name="tematica_id", referencedColumnName = "id")
    private TematicaEstudio tematica;

    @Setter
    @ManyToOne
    @JoinColumn(name="config_id", referencedColumnName = "id")
    private Configurador preferencia;

    @Setter
    @OneToMany
    @JoinColumn(name="memocard_no_vista_id", referencedColumnName = "id")
    private Set<MemoCard> memoCardsNoVistas;

    @Setter
    @OneToMany
    @JoinColumn(name="memocard_vista_id", referencedColumnName = "id")
    private Set<MemoCard> memoCardsVistas;

    @Setter
    @OneToMany
    @JoinColumn(name="memocard_activa_id", referencedColumnName = "id")
    private CircularList<MemoCard> memoCardsEnRepasoActivo;

    @Setter
    @Column(name="fecha_ultima_nueva_inclusion")
    private LocalDateTime fechaUltimaInclusionNuevasMemoCards;

    // Métodos

    public Mazo() {
        memoCardsNoVistas = new HashSet<>();
        memoCardsVistas = new HashSet<>();
        memoCardsEnRepasoActivo = new CircularList<>();
    }


    public void agregarMemoCard(MemoCard... memoCard) {
        this.memoCardsNoVistas.addAll(Arrays.asList(memoCard));
        fechaUltimaInclusionNuevasMemoCards = LocalDateTime.now();
    }

    public Boolean laMemoCardEsExistente(MemoCard memoCard) {
        return (this.buscarMemoCardPorId(memoCard.getId()) != null) || (this.buscarMemoCardPorNombre(memoCard.getNombre()) != null);
    }

    private MemoCard buscarMemoCardPorNombre(String nombreBuscado) {
        Set<MemoCard> todasMemoCards = new HashSet<>(this.memoCardsNoVistas); // Copia el primer conjunto
        todasMemoCards.addAll(memoCardsVistas);
        return todasMemoCards.stream().filter(memoCard -> memoCard.getNombre().equals(nombreBuscado)).findFirst().orElse(null);
    }

    private MemoCard buscarMemoCardPorId(Long id) {
        Set<MemoCard> todasMemoCards = new HashSet<>(this.memoCardsNoVistas); // Copia el primer conjunto
        todasMemoCards.addAll(memoCardsVistas);
        return todasMemoCards.stream().filter(memoCard -> memoCard.getId().equals(id)).findFirst().orElse(null);
    }

    public void eliminarMemoCard(MemoCard memoCard) {
        this.memoCardsNoVistas.remove(memoCard);
        this.memoCardsVistas.remove(memoCard);
    }

    public void repasarMemoCard(MemoCard memoCard) {
        this.memoCardsNoVistas.remove(memoCard);
        this.memoCardsEnRepasoActivo.remove(memoCard);

        memoCard.setFechaUltimoRepaso(LocalDateTime.now());

        this.memoCardsVistas.add(memoCard);
    }

    public Set<MemoCard> generarBarajaDeMemoCards() {
        var barajador = preferencia.getBarajador();
        //Filtro las memoCards que estan Listas para Repasar
        var memoCardsVistasPorRevisar = memoCardsVistas.stream().filter(MemoCard::estaListaParaRepasar).toList();

        //Seleccionamos una cantidad de memoCards segun las preferencias del Usuario
        var maximoARepasar = memoCardsVistasPorRevisar.size() >= preferencia.getMaximoDeCartasARepasar()? preferencia.getMaximoDeCartasARepasar() : memoCardsVistasPorRevisar.size();
        memoCardsVistasPorRevisar = memoCardsVistasPorRevisar.subList(0,maximoARepasar);
        var maximoNuevas = memoCardsNoVistas.size() >= preferencia.getMaximoDeNuevasCartas()? preferencia.getMaximoDeNuevasCartas() : memoCardsNoVistas.size();
        var nuevasMemocardsPorRepasar = memoCardsNoVistas.stream().toList().subList(0,maximoNuevas);

        this.memoCardsEnRepasoActivo = barajador.barajarComienzoDeSesion(nuevasMemocardsPorRepasar,memoCardsVistasPorRevisar,memoCardsEnRepasoActivo);

        return new HashSet<>((memoCardsEnRepasoActivo).getElements());
    }
}
