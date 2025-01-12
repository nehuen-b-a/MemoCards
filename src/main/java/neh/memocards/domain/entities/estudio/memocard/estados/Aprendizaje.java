package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;


public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    private final List<Long> intervalos;
    private List<Long> intervalosBonificados;
    private Integer cantidadDeAciertos;

    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard,"APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.cantidadDeAciertos = 0;
    }

    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        // En caso de Mucha dificultad Reseteamos los aciertos y las bonificaciones
        this.cantidadDeAciertos = dificultad <= 2 ? cantidadDeAciertos + 1 : 0;
        this.intervalosBonificados = dificultad >= 3 ? this.intervalos: this.intervalosBonificados;

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados.forEach(intervalo -> super.bonificarIntervalo(intervalo, dificultad));
        Long intervaloHipotetico = this.intervalosBonificados.get(cantidadDeAciertos);

        //Nos fijamos si superamos el umbral de Aprendizaje, si pase eso pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (intervaloHipotetico >= intervalos.get(intervalos.size() - 1)) {
            this.actualizarEstado();
            Long nuevoIntervalo = super.getMemoCard().getEstadoAprendizaje().calcularIntervalo(intervaloHipotetico, dificultad);
            this.setIntervaloActual(nuevoIntervalo);
            return nuevoIntervalo;
        } else {
            this.setIntervaloActual(intervaloHipotetico);
            return intervaloHipotetico;
        }
    }


    @Override
    public void actualizarEstado() {super.getMemoCard().cambiarEstado(new Repaso(super.getMemoCard())); }



}
