package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

@Setter
@Getter
public abstract class EstadoMemoCard {
    // Atributos
    private Long id;
    private String nombre;
    private MemoCard memoCard;

    // Métodos
    public abstract void calcularIntervalo(Integer intervalo, Integer dificultad);

    public abstract void actualizarEstado(EstadoMemoCard nuevoEstado);

    public EstadoMemoCard(MemoCard memoCard, String nombre) {
        this.memoCard = memoCard;
        this.nombre = nombre;
    }
}