package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

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

    private Set<MemoCard> tarjetasNoVistas;

    private Set<MemoCard> tarjetasVistas;

    private List<SesionDeEstudio> historialDeEstudio;

    // Métodos

    public Mazo() {
        historialDeEstudio = new ArrayList<>();
        tarjetasNoVistas = new HashSet<>();
        tarjetasVistas = new HashSet<>();
    }

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
