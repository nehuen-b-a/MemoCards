package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.ArrayList;
import java.util.List;

@Getter

public class Reaprendizaje extends EstadoMemoCard {

    // Atributos
    private List<Long> intervalos;
    private List<Long> intervalosBonificados;
    private Double distanciaPorcentualIntervalo;
    private Long intervaloMin;
    private Double bonificacionTotal;


    // Métodos

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard, "REAPRENDIZAJE");

        //cargo la configuracion
        Configurador configurador = memoCard.getConfigurador();
        this.intervaloMin = configurador.getIntervaloMinimo();
        this.distanciaPorcentualIntervalo = configurador.getDistanciaPorcentualIntervalo();
        this.intervalos = configurador.getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.bonificacionTotal = 1d;

        //ajustamos el intervalo al minimo de la configuracion
        this.ajustarIntervaloMinimo();
    }

    public Reaprendizaje(MemoCard memoCard, Long intervaloActual, Integer cantidadDeAciertos, Integer cantidadDeDesaciertos) {
        this(memoCard);
        this.setIntervaloActual(intervaloActual);
        this.setRachaAciertos(cantidadDeAciertos);
        this.setRachaDesaciertos(cantidadDeDesaciertos);
    }

    @Override
    public Long cambiarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        //Se agrega el intento
        memoCard.setIntentos(memoCard.getIntentos() + 1);
        // En caso de mucha dificultad Reseteamos y le damos el minimo
        if (dificultad == Dificultad.OLVIDO || rachaAciertos == 0) {
            this.intervalosBonificados = this.intervalos;
            this.rachaAciertos = dificultad == Dificultad.OLVIDO ? 0 : rachaAciertos + 1;
            rachaDesaciertos = dificultad == Dificultad.OLVIDO ? rachaDesaciertos + 1 : rachaDesaciertos;
            super.getMemoCard().actualizarSaguijela();
            this.intervaloActual = estimarIntervalo(intervaloAnterior, dificultad);
            this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
            actualizarBonificacionTotal(dificultad);
            actualizarSesion(dificultad);
            return intervaloActual;
        }

        this.rachaAciertos++;
        this.rachaDesaciertos = 0;
        super.getMemoCard().actualizarSaguijela();

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();

        //Nos fijamos si superamos el umbral de Aprendizaje, entonces pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (rachaAciertos > intervalos.size()) {
            this.actualizarEstado();
            super.getMemoCard().getEstadoMemoCard().cambiarIntervalo(intervaloAnterior, dificultad);
            actualizarSesion(dificultad);
            return memoCard.getEstadoMemoCard().getIntervaloActual();
        } else {
            this.intervaloActual = estimarIntervalo(intervaloAnterior, dificultad);
            actualizarBonificacionTotal(dificultad);
            actualizarSesion(dificultad);
            return super.getIntervaloActual();
        }
    }

    @Override
    public Long estimarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        if (dificultad == Dificultad.OLVIDO || rachaAciertos == 0) {
            return intervalosBonificados.get(0);
        }
        if (rachaAciertos > intervalos.size()) {
            Repaso repaso = new Repaso(
                    super.getMemoCard(),
                    this.intervaloActual,
                    this.rachaAciertos + 1,
                    this.rachaDesaciertos
            );
            return repaso.estimarIntervalo(intervaloAnterior, dificultad);
        } else {
            return intervalosBonificados.get(rachaAciertos - 1);
        }
    }

    @Override
    public void actualizarEstado() {
        memoCard.cambiarEstado(new Repaso(
                super.getMemoCard(),
                this.intervaloActual,
                this.rachaAciertos - 1,
                this.rachaDesaciertos));
    }

    @Override
    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        this.intervalos = configurador.getIntervaloInicial();
        this.distanciaPorcentualIntervalo = configurador.getDistanciaPorcentualIntervalo();
        this.intervaloMin = configurador.getIntervaloMinimo();

        this.ajustarIntervaloMinimo();
    }

    private void ajustarIntervaloMinimo() {
        List<Long> intervaloAux = new ArrayList<>();
        long referenciaSuperior = (long) ((double) intervaloMin * distanciaPorcentualIntervalo);

        //Reemplazamos tanto los elementos mas chicos como los que se acercan mucho a nuestro intervalo minimo
        intervaloAux.add(this.intervaloMin);
        intervaloAux.addAll(this.intervalos.stream().filter(valor -> valor > referenciaSuperior).toList());
        this.intervalos = intervaloAux;

        intervalosBonificados = intervalos.stream().map(intv -> (long) (intv * bonificacionTotal)).toList();
    }

    private void actualizarBonificacionTotal(Dificultad dificultad) {
        this.bonificacionTotal *= (double) (super.bonificarIntervalo(100000L, dificultad)) / 100000d;
    }


}
