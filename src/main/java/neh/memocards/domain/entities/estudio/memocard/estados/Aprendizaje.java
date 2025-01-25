package neh.memocards.domain.entities.estudio.memocard.estados;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("aprendizaje")
public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    @ElementCollection
    @CollectionTable(
            name = "estado_intervalos",
            joinColumns = @JoinColumn(name = "config_id")
    )
    @Column(name = "intervalos")
    private List<Long> intervalos;

    @ElementCollection
    @CollectionTable(
            name = "estado_intervalos_bonificados",
            joinColumns = @JoinColumn(name = "config_id")
    )
    private List<Long> intervalosBonificados;



    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.intervalosBonificados = this.intervalos;
        this.rachaAciertos = 0;
        this.rachaDesaciertos = 0;
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
            actualizarSesion(dificultad);
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
            actualizarSesion(dificultad);
            return memoCard.getEstadoMemoCard().getIntervaloActual();

        } else {
            this.intervaloActual = estimarIntervalo(intervaloAnterior, dificultad);
            actualizarBonificacionTotal(dificultad);
            actualizarSesion(dificultad);
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
