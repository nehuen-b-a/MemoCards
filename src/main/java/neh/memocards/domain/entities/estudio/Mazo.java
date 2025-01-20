package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;

import java.util.*;
@Getter
public class Mazo {
    // Atributos
    @Setter
    private Long id;
    @Setter
    private String nombre;
    @Setter
    private String descripcion;
    @Setter
    private Configurador preferencia;

    private Set<MemoCard> memoCardsNoVistas;

    private Set<MemoCard> memoCardsVistas;

    private CircularList<MemoCard> a;

    // Métodos

    public Mazo() {
        memoCardsNoVistas = new HashSet<>();
        memoCardsVistas = new HashSet<>();
    }

    public void iniciarSesionDeEstudio() {
        // Implementación pendiente
    }

    public void agregarMemoCard(MemoCard ... memoCard) {
        this.memoCardsNoVistas.addAll(Arrays.asList(memoCard));
    }

    public Boolean laMemoCardEsExistente (MemoCard memoCard) {
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

    public void marcarTarjetaComoVista(MemoCard memoCard) {
        this.memoCardsVistas.add(memoCard);
        this.memoCardsNoVistas.remove(memoCard);
    }
    public void marcarTarjetaComoVista(Collection<MemoCard> memoCards) {
        this.memoCardsVistas.addAll(memoCards);
        this.memoCardsNoVistas.removeAll(memoCards);
    }

    public void eliminarMemoCard(MemoCard memoCard) {
        this.memoCardsNoVistas.remove(memoCard);
        this.memoCardsVistas.remove(memoCard);
    }
}
