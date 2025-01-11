package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reaprendizaje extends EstadoMemoCard {
    // Atributos
    private List<Long> intervalos;
    private Long intevaloMin;
    private Integer cantidadDeAciertos;

    // Métodos
    @Override
    public void calcularIntervalo(Integer intervalo, Integer dificultad) {
        this.cantidadDeAciertos =  dificultad <= 2 ? cantidadDeAciertos + 1: 0;
        if(this.intervalos.get(cantidadDeAciertos) >= intervalos.get(cantidadDeAciertos -1)){
            super.getMemoCard().cambiarEstado(new Repaso(super.getMemoCard()));
        }
    }

    @Override
    public void actualizarEstado(EstadoMemoCard nuevoEstado) {
        // Implementación pendiente
    }

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard,"REAPRENDIZAJE");

        this.intervalos = memoCard.getConfigurador().getIntervaloInicial()
                .stream()
                .filter(intervalo -> intervalo >= 1440L)
                .collect(Collectors.toList());

        if(intervalos.stream().anyMatch(intervalo -> (intervalo >= 1440L && intervalo <= 2500L ))){
            List<Long> intervaloAux = new ArrayList<>();
            intervaloAux.add(1440L);
            intervaloAux.addAll(intervalos);
            this.intervalos = intervaloAux;
        }

        this.cantidadDeAciertos = 0;
    }
}
