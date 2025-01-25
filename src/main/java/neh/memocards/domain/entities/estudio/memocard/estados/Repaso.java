package neh.memocards.domain.entities.estudio.memocard.estados;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;


@Getter
@NoArgsConstructor
@Entity
@DiscriminatorValue("repaso")
public class Repaso extends EstadoMemoCard {
    // Atributos
    private Double coeficienteDeRetencion;
    private Long intervaloMax;

    // Métodos
    @Override
    public Long cambiarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        if (dificultad == Dificultad.OLVIDO) {
            this.rachaAciertos = 0;
            this.actualizarEstado();
            actualizarSesion(dificultad);
            return super.getMemoCard().getEstadoMemoCard().cambiarIntervalo(intervaloAnterior, dificultad);
        }
        this.rachaAciertos++;
        this.intervaloActual = this.estimarIntervalo(intervaloAnterior, dificultad);
        actualizarSesion(dificultad);
        return intervaloActual;
    }

    @Override
    public Long estimarIntervalo(Long intervaloAnterior, Dificultad dificultad) {
        if (dificultad == Dificultad.OLVIDO) {
            Reaprendizaje reaprendizaje = (new Reaprendizaje(
                    super.getMemoCard(),
                    this.intervaloActual,
                    this.rachaAciertos,
                    this.rachaDesaciertos));
            return reaprendizaje.estimarIntervalo(intervaloAnterior, dificultad);
        }
        return (double) (intervaloAnterior) * coeficienteDeRetencion < this.intervaloMax ?
                super.bonificarIntervalo((long) ((double) (intervaloAnterior) * coeficienteDeRetencion), dificultad)
                : this.intervaloMax;
    }

    @Override
    public void actualizarEstado() {
        memoCard.cambiarEstado(new Reaprendizaje(
                super.getMemoCard(),
                this.intervaloActual,
                this.rachaAciertos,
                this.rachaDesaciertos));
    }

    public Repaso(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.coeficienteDeRetencion = memoCard.getConfigurador().getCoeficienteDeRetencion();
        this.intervaloMax = memoCard.getConfigurador().getIntervaloMaximo();
    }

    public Repaso(MemoCard memoCard, Long intervaloActual, Integer cantidadDeAciertos, Integer cantidadDeDesaciertos) {
        this(memoCard); // Llama al constructor base
        this.intervaloActual = intervaloActual;
        this.rachaAciertos = cantidadDeAciertos;
        this.rachaDesaciertos = cantidadDeDesaciertos;
    }

    @Override
    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        this.coeficienteDeRetencion = configurador.getCoeficienteDeRetencion();
        this.intervaloMax = configurador.getIntervaloMaximo();
    }

}
