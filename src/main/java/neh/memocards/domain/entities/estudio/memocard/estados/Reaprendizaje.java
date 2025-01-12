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
    public Long calcularIntervalo(Long intervalo, Integer dificultad) {
        this.cantidadDeAciertos = dificultad <= 2 ? cantidadDeAciertos + 1 : 0;
        Long intervaloActual = this.intervalos.get(cantidadDeAciertos);

        if (intervaloActual >= intervalos.get(cantidadDeAciertos - 1)) {
            this.actualizarEstado();
            Long nuevoIntervalo = super.getMemoCard().getEstadoAprendizaje().calcularIntervalo(intervaloActual, dificultad);
            this.setIntervaloActual(nuevoIntervalo);
            return nuevoIntervalo;

        } else {
            Long nuevoIntervalo = super.bonificarIntervalo(intervaloActual, dificultad);
            this.setIntervaloActual(nuevoIntervalo);
            return nuevoIntervalo;

        }
    }

    @Override
    public void actualizarEstado() {super.getMemoCard().cambiarEstado(new Repaso(super.getMemoCard())); }

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard,"REAPRENDIZAJE");

        this.intervalos = memoCard.getConfigurador().getIntervaloInicial()
                .stream()
                .filter(intervalo -> intervalo >= this.intevaloMin)
                .collect(Collectors.toList());

        if(intervalos.stream().anyMatch(intervalo -> (intervalo >= this.intevaloMin && intervalo <= this.intevaloMin*1.6))){
            List<Long> intervaloAux = new ArrayList<>();
            intervaloAux.add(this.intevaloMin);
            intervaloAux.addAll(intervalos);
            this.intervalos = intervaloAux;
        }

        this.cantidadDeAciertos = 0;
    }
}
