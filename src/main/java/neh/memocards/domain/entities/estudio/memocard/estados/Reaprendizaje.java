package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reaprendizaje extends EstadoMemoCard {
    // Atributos
    private List<Long> intervalos;
    private final Long intevaloMin;
    private List<Long> intervalosBonificados;
    private Integer cantidadDeAciertos;

    // Métodos

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard, "REAPRENDIZAJE");
        this.intevaloMin = memoCard.getConfigurador().getIntervaloMinimo();

        this.intervalos = memoCard.getConfigurador().getIntervaloInicial()
                .stream()
                .filter(intervalo -> intervalo >= this.intevaloMin)
                .collect(Collectors.toList());

        if (intervalos.stream().anyMatch(intervalo -> (intervalo >= this.intevaloMin && intervalo <= this.intevaloMin * 1.6))) {
            List<Long> intervaloAux = new ArrayList<>();
            intervaloAux.add(this.intevaloMin);
            intervaloAux.addAll(intervalos);
            this.intervalos = intervaloAux;
        }
        this.intervalosBonificados = this.intervalos;
        this.cantidadDeAciertos = 0;
    }

    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        // En caso de Mucha dificultad Reseteamos los aciertos y las bonificaciones
        this.cantidadDeAciertos = dificultad <= 2 ? cantidadDeAciertos + 1 : 0;
        this.intervalosBonificados = dificultad >= 3 ? this.intervalos : this.intervalosBonificados;

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados.forEach(intervalo -> super.bonificarIntervalo(intervalo, dificultad));
        Long intervaloHipotetico = this.intervalosBonificados.get(cantidadDeAciertos);

        //Nos fijamos si superamos el umbral de Aprendizaje, si ocurre eso pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
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
