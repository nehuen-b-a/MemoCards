package neh.memocards.domain.entities.estudio.memocard.estados;


import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;


public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    private List<Long> intervalos;
    private Integer cantidadDeAciertos;

    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard,"APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.cantidadDeAciertos = 0;
    }

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



}
