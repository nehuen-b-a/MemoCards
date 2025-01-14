package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
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
        System.out.println("Creo REAPRENDZAJE");
        Configurador configurador = memoCard.getConfigurador();
        this.intervaloMin = configurador.getIntervaloMinimo();
        this.distanciaPorcentualIntervalo = configurador.getDistanciaPorcentualIntervalo();
        this.intervalos = configurador.getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.bonificacionTotal = 1d;

        //ajustamos el intervalo al minimo de la configuracion
        this.ajustarIntervaloMinimo();

        System.out.println("Se creo los intervalos: " + intervalos);
        System.out.println("Se creo los intervalosbonificados: " + intervalosBonificados);
    }

    public Reaprendizaje(MemoCard memoCard, Long intervaloActual, Integer cantidadDeAciertos, Integer cantidadDeDesaciertos) {
        this(memoCard);
        this.setIntervaloActual(intervaloActual);
        this.setRachaAciertos(cantidadDeAciertos);
        this.setRachaDesaciertos(cantidadDeDesaciertos);
    }

    @Override
    public Long cambiarIntervalo(Long intervaloAnterior, Integer dificultad) {
        //Se agrega el intento
        memoCard.setIntentos(memoCard.getIntentos() + 1);
        // En caso de mucha dificultad Reseteamos y le damos el minimo
        if (dificultad >= 3 || rachaAciertos == 0) {
            this.intervalosBonificados = this.intervalos;
            this.rachaAciertos = dificultad >= 3? 0 : rachaAciertos + 1;
            rachaDesaciertos = dificultad >= 3? rachaDesaciertos + 1 : rachaDesaciertos;
            super.getMemoCard().actualizarSaguijela();
            this.intervaloActual = this.intervalos.get(0);
            this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
            actualizarBonificacionTotal(dificultad);
            return intervaloActual;
        }

        this.rachaAciertos++;
        this.rachaDesaciertos = 0;
        super.getMemoCard().actualizarSaguijela();

        System.out.println(intervalosBonificados);

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();

        System.out.println(intervalosBonificados);

        //Nos fijamos si superamos el umbral de Aprendizaje, entonces pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (rachaAciertos > intervalos.size()) {
            this.actualizarEstado();
            System.out.println("CAMBIO-DE-ESTADO");
            super.getMemoCard().getEstadoMemoCard().cambiarIntervalo(intervaloAnterior, dificultad);
            return memoCard.getEstadoMemoCard().getIntervaloActual();
        } else {
            this.intervaloActual = intervalosBonificados.get(rachaAciertos -1);
            actualizarBonificacionTotal(dificultad);
            return super.getIntervaloActual();
        }
    }

    @Override
    public Long estimarIntervalo(Long intervaloAnterior, Integer dificultad) {
        return 0L;
    }

    @Override
    public void actualizarEstado() {
        super.getMemoCard().cambiarEstado(new Repaso(
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

        System.out.println("Inertvalo Antes de Transformarlo: " + this.intervalos );
        System.out.println("InertvaloBonficado Antes de Transformarlo: " + this.intervalosBonificados );

        this.ajustarIntervaloMinimo();

        System.out.println("Inertvalo Despues de Transformarlo: " + this.intervalos );
        System.out.println("InertvaloBonficado Despues de Transformarlo: " + this.intervalosBonificados );
    }

    private void ajustarIntervaloMinimo(){
        List<Long> intervaloAux = new ArrayList<>();
        long referenciaSuperior = (long)((double)intervaloMin * distanciaPorcentualIntervalo);

            System.out.println("ReferenciaSuperior: " + referenciaSuperior);
            System.out.println("antesDeReemplazo: " + this.intervalos);
            System.out.println("antesDeReemplazoBonificado: " + this.intervalosBonificados);

        //Reemplazamos tanto los elementos mas chicos como los que se acercan mucho a nuestro intervalo minimo
        intervaloAux.add(this.intervaloMin);
        intervaloAux.addAll(this.intervalos.stream().filter(valor -> valor > referenciaSuperior).toList());
        this.intervalos = intervaloAux;

        intervalosBonificados = intervalos.stream().map(intv -> (long) (intv * bonificacionTotal)).toList();

        System.out.println("luegoDeReemplazo: " + this.intervalos);
        System.out.println("luegoDeReemplazoBonificado: " + this.intervalosBonificados);
    }

    private void actualizarBonificacionTotal(Integer dificultad){
        this.bonificacionTotal *= (double)(super.bonificarIntervalo(100000L,dificultad))/100000d;
    }



}
