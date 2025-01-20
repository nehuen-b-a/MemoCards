package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;

@Getter
public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    private List<Long> intervalos;
    private List<Long> intervalosBonificados;
    private Double bonificacionTotal;


    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.rachaAciertos = 0;
        this.rachaDesaciertos = 0;
        this.bonificacionTotal = 1d;
    }

    @Override
    public Long cambiarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        //Se agrega el intento
        memoCard.setIntentos(memoCard.getIntentos() + 1);
        // En caso de la primera asignacion o mucha dificultad Reseteamos y le damos el minimo
        if (memoCard.getIntentos() == 1 || dificultad == Dificultad.OLVIDO) {
            this.intervalosBonificados = this.intervalos;
            this.rachaAciertos = 0;
            rachaDesaciertos = dificultad == Dificultad.OLVIDO ? rachaDesaciertos + 1 : 0;
            memoCard.actualizarSaguijela();
            this.intervaloActual = estimarIntervalo(intervaloAnterior, dificultad);
            this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();
            actualizarBonificacionTotal(dificultad);
            memoCard.getSesionDeEstudioActual().registrarMetrica(memoCard,dificultad);
            return intervaloActual;
        }

        this.rachaAciertos++;
        this.rachaDesaciertos = 0;
        super.getMemoCard().actualizarSaguijela();

        //bonificamos intervalos segun dificultad
        this.intervalosBonificados = this.intervalosBonificados.stream().map(intervalo -> super.bonificarIntervalo(intervalo, dificultad)).toList();


        //Nos fijamos si superamos el umbral de Aprendizaje, entonces pasamos a Repaso, caso contratio retornamos el intevaloHipotetico
        if (rachaAciertos > intervalos.size() - 1) {
            this.actualizarEstado();
            super.getMemoCard().getEstadoMemoCard().cambiarIntervalo(intervaloAnterior, dificultad);
            memoCard.getSesionDeEstudioActual().registrarMetrica(memoCard,dificultad);
            return memoCard.getEstadoMemoCard().getIntervaloActual();

        } else {
            this.intervaloActual = estimarIntervalo(intervaloAnterior, dificultad);
            actualizarBonificacionTotal(dificultad);
            memoCard.getSesionDeEstudioActual().registrarMetrica(memoCard,dificultad);
            return this.getIntervaloActual();
        }
    }

    @Override
    public Long estimarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        if (memoCard.getIntentos() == 1 || dificultad == Dificultad.OLVIDO) {
            return intervalosBonificados.get(0);
        }
        if (rachaAciertos > intervalos.size() - 1) {
            Repaso repaso = new Repaso(
                    super.getMemoCard(),
                    this.intervaloActual,
                    this.rachaAciertos + 1,
                    this.rachaDesaciertos
            );
            return repaso.estimarIntervalo(intervaloAnterior, dificultad);
        } else {
            return intervalosBonificados.get(rachaAciertos);
        }
    }


    @Override
    public void actualizarEstado() {
        Repaso nuevoEstado = new Repaso(
                this.memoCard,
                this.intervaloActual,
                this.rachaAciertos - 1,
                this.rachaDesaciertos
        );
        super.getMemoCard().cambiarEstado(nuevoEstado);
    }

    @Override
    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        intervalos = configurador.getIntervaloInicial();
        intervalosBonificados = intervalos.stream().map(valor -> (long) (valor * bonificacionTotal)).toList();
    }

    private void actualizarBonificacionTotal(Dificultad dificultad) {
        this.bonificacionTotal *= (double) (super.bonificarIntervalo(100000L, dificultad)) / 100000d;
    }


}
