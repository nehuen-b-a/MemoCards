package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;

@Setter
@Getter
public abstract class EstadoMemoCard {
    // Atributos
    private Long id;
    private String nombre;
    private Long intervaloActual ;
    private MemoCard memoCard;

    // Métodos
    public abstract Long calcularIntervalo(Long intervalo,Integer dificultad);

    public abstract void actualizarEstado();


    public EstadoMemoCard(MemoCard memoCard, String nombre) {
        this.memoCard = memoCard;
        this.nombre = nombre;
    }

    protected Long bonificarIntervalo(Long intervalo, Integer dificultad) {
        Double bonificacion;
        switch (dificultad) {
            case 0: bonificacion = this.getMemoCard().getConfigurador().getBonusFacil();
            case 2: bonificacion = this.getMemoCard().getConfigurador().getIntervaloDificil();
            default: bonificacion = 1d;
        }

        return (long)((double)(intervalo) * bonificacion);
    }
}