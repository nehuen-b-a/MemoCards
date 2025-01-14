package neh.memocards.domain.entities.estudio.memocard.estados;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter

public class Reaprendizaje extends EstadoMemoCard {

    // Atributos
    private List<Long> intervalos;
    private final Long intervaloMin;
    private List<Long> intervalosBonificados;

    // Métodos

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard, "REAPRENDIZAJE");
        this.intervaloMin = memoCard.getConfigurador().getIntervaloMinimo();

        this.intervalos = memoCard.getConfigurador().getIntervaloInicial()
                .stream()
                .filter(intervalo -> intervalo >= this.intervaloMin)
                .collect(Collectors.toList());

        Integer pos = this.intervalos.stream()
                .filter(intervalo -> intervalo >= this.intervaloMin && intervalo <= this.intervaloMin * 1.6)
                .findFirst()
                .map(this.intervalos::indexOf)
                .orElse(-1);
        if (pos != 1) {
            List<Long> intervaloAux = new ArrayList<>();
            intervaloAux.add(this.intervaloMin);
            intervaloAux.addAll(this.intervalos.subList(pos + 1, this.intervalos.size()));
            this.intervalos = intervaloAux;
        }
        this.intervalosBonificados = this.intervalos;
    }

    public Reaprendizaje(MemoCard memoCard, Long intervaloActual, Integer intentos, Integer cantidadDeAciertos, Integer cantidadDeDesaciertos) {
        this(memoCard);
        this.setIntervaloActual(intervaloActual);
        this.setIntentos(intentos);
        this.setCantidadDeAciertos(cantidadDeAciertos);
        this.setCantidadDeDesaciertos(cantidadDeDesaciertos);
    }

    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        //Se agrega el intento
        super.setIntentos(super.getIntentos() + 1);
        // En caso de la primera asignacion o mucha dificultad Reseteamos y le damos el minimo
        if (super.getIntentos() == 1 || dificultad >= 3) {
            this.intervalosBonificados = this.intervalos;
            this.cantidadDeAciertos = 0;
            cantidadDeDesaciertos = dificultad >= 3 ? cantidadDeDesaciertos + 1 : 0;
            super.getMemoCard().actualizarSaguijela();
            Long intervaloActual = this.intervalos.get(0);
            this.intervaloActual = intervaloActual;
            this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
            return intervaloActual;
        }

        this.cantidadDeAciertos++;
        this.cantidadDeDesaciertos = 0;
        super.getMemoCard().actualizarSaguijela();

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();


        //Nos fijamos si superamos el umbral de Aprendizaje, entonces eso pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (cantidadDeAciertos > intervalos.size() - 1) {
            this.actualizarEstado();
            super.getMemoCard().getEstadoAprendizaje().calcularIntervalo(intervaloAnterior, dificultad);
            return super.getIntervaloActual();

        } else {
            this.intervaloActual = intervalosBonificados.get(cantidadDeAciertos);
            return super.getIntervaloActual();

        }
    }

    @Override
    public void actualizarEstado() {
        super.getMemoCard().cambiarEstado(new Repaso(
                super.getMemoCard(),
                this.intervaloActual,
                this.intentos,
                this.cantidadDeAciertos,
                this.cantidadDeDesaciertos));
    }

    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        this.intervalos = configurador.getIntervaloInicial();

        double relacion;
        relacion = (double) intervalosBonificados.get(0) / (double) (intervalos.get(0));

        intervalosBonificados = intervalosBonificados.stream().map(intv -> (long) (intv * relacion)).toList();
    }


}
