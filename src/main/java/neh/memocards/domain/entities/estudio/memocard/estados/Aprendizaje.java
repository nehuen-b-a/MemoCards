package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.AllArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;


public class Aprendizaje extends EstadoMemoCard {
    // Atributos
    private Long umbralIntevaloMax;
    private Integer cantidadDeAciertos;

    // Métodos
    @Override
    public void calcularIntervalo(Integer intervalo, Integer dificultad) {
        // Implementación pendiente
    }

    @Override
    public void actualizarEstado(EstadoMemoCard nuevoEstado) {
        // Implementación pendiente
    }

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard,"APRENDIZAJE");

        List <Long> intervaloInicial = memoCard.getConfigurador().getIntervaloInicial();
        this.umbralIntevaloMax = intervaloInicial.get(intervaloInicial.size() - 1);

        this.cantidadDeAciertos = 0;
    }
}
