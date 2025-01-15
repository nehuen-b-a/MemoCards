package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;

@Getter
public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    private List<Long> intervalos;
    private List<Long> intervalosBonificados;


    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.cantidadDeAciertos = 0;
        this.cantidadDeDesaciertos = 0;
    }

    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        //Se agrega el intento
        memoCard.setIntentos(memoCard.getIntentos() + 1);
        // En caso de la primera asignacion o mucha dificultad Reseteamos y le damos el minimo
        if (memoCard.getIntentos() == 1 || dificultad >= 3) {
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


        //Nos fijamos si superamos el umbral de Aprendizaje, entonces pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (cantidadDeAciertos > intervalos.size() - 1) {
            this.actualizarEstado();
            super.getMemoCard().getEstadoMemoCard().calcularIntervalo(intervaloAnterior, dificultad);
            return super.getIntervaloActual();

        } else {
            this.intervaloActual = intervalosBonificados.get(cantidadDeAciertos);
            System.out.println(intervalosBonificados.get(cantidadDeAciertos));
            return super.getIntervaloActual();
        }
    }


    @Override

    public void actualizarEstado() {
        Repaso nuevoEstado = new Repaso(
                this.memoCard,
                this.intervaloActual,
                this.cantidadDeAciertos - 1,
                this.cantidadDeDesaciertos
        );
        super.getMemoCard().cambiarEstado(nuevoEstado);
    }

    @Override
    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        intervalos = configurador.getIntervaloInicial();

        double relacion;
        relacion = (double) intervalosBonificados.get(0) / (double) (intervalos.get(0));

        intervalosBonificados = intervalosBonificados.stream().map(intv -> (long) (intv * relacion)).toList();
    }


}
