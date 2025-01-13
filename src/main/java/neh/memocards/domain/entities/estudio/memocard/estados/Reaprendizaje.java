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
        // En caso de la primera asignacion le damos el minimo
        if (cantidadDeAciertos == 0) {
            Long intervaloActual = this.intervalos.get(0);
            super.setIntervaloActual(intervaloActual);
            this.cantidadDeAciertos = dificultad <= 2 ? cantidadDeAciertos + 1 : 0;
            this.intervalosBonificados= this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
            System.out.println("IntervaloT=0: "+ intervalosBonificados);
            return intervaloActual;
        }

        // En caso de Mucha dificultad Reseteamos los aciertos y las bonificaciones
        if(dificultad >= 3) {
            this.intervalosBonificados = this.intervalos;
            this.cantidadDeAciertos = 0;
        }

        //bonificamos intervalos segun dificultad
        System.out.println("IntervaloT=1: " + intervalosBonificados);
        this.intervalosBonificados= this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
        Long intervaloHipotetico = cantidadDeAciertos <= (intervalos.size() - 1)? this.intervalosBonificados.get(cantidadDeAciertos) : intervalosBonificados.get(intervalos.size() - 1);
        System.out.println("IntervaloT=2: "
                + intervalosBonificados);

        System.out.println("IntervaloPrueba1: 1000, Dificultad: 2");
        System.out.println("IntervaloPrueba1: " +super.bonificarIntervalo(1000L,2) +", Dificultad: 2");

        //Nos fijamos si superamos el umbral de Aprendizaje, si pase eso pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (intervaloHipotetico >= intervalos.get(intervalos.size() - 1)) {
            this.actualizarEstado();
            this.setIntervaloActual(intervaloHipotetico);

            return intervaloHipotetico;

        } else {
            this.setIntervaloActual(intervaloHipotetico);
            this.cantidadDeAciertos ++;
            return intervaloHipotetico;
        }
    }

    @Override
    public void actualizarEstado() {super.getMemoCard().cambiarEstado(new Repaso(super.getMemoCard())); }


}
