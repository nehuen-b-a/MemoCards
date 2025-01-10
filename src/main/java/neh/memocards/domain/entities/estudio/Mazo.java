package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.*;

public class Mazo {
    // Atributos
    @Setter @Getter
    private Long id;
    @Setter @Getter
    private String nombre;
    @Setter @Getter
    private String descripcion;
    @Getter
    private Set<MemoCard> tarjetasNoVistas;
    @Getter
    private Set<MemoCard> tarjetasVistas;
    @Getter
    private List<SesionDeEstudio> historialDeEstudio;

    // Métodos
    public void iniciarSesionDeEstudio() {
        // Implementación pendiente
    }

    public void agregarMemoCard(MemoCard ... memoCard) {
        this.tarjetasNoVistas.addAll(Arrays.asList(memoCard));
    }

    public Boolean laMemoCardEsExistente (MemoCard memoCard) {
        return (this.buscarMemoCardPorId(memoCard.getId()) != null) || (this.buscarMemoCardPorNombre(memoCard.getNombre()) != null);
    }

    private MemoCard buscarMemoCardPorNombre(String nombreBuscado) {
        Set<MemoCard> todasMemoCards = new HashSet<>(this.tarjetasNoVistas); // Copia el primer conjunto
        todasMemoCards.addAll(tarjetasVistas);
        return todasMemoCards.stream().filter(memoCard -> memoCard.getNombre().equals(nombreBuscado)).findFirst().orElse(null);
    }
    private MemoCard buscarMemoCardPorId(Long id) {
        Set<MemoCard> todasMemoCards = new HashSet<>(this.tarjetasNoVistas); // Copia el primer conjunto
        todasMemoCards.addAll(tarjetasVistas);
        return todasMemoCards.stream().filter(memoCard -> memoCard.getId().equals(id)).findFirst().orElse(null);
    }

    public void marcarTarjetaComoVista(MemoCard memoCard) {
        this.tarjetasVistas.add(memoCard);
        this.tarjetasNoVistas.remove(memoCard);
    }
    public void marcarTarjetaComoVista(Collection<MemoCard> memoCards) {
        this.tarjetasVistas.addAll(memoCards);
        this.tarjetasNoVistas.removeAll(memoCards);
    }
}
