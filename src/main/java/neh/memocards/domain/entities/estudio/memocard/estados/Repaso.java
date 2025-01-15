package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;


@Getter

public class Repaso extends EstadoMemoCard {
    // Atributos
    private Double coeficienteDeRetencion;
    private Long intervaloMax;

    // Métodos
    @Override
    public Long cambiarIntervalo(Long intervaloAnterior, Integer dificultad) {
        if (dificultad >= 3) {
            this.actualizarEstado();
            return super.getMemoCard().getEstadoMemoCard().cambiarIntervalo(intervaloAnterior, dificultad);
        }
        this.rachaAciertos++;
        this.intervaloActual = (double) (intervaloAnterior) * coeficienteDeRetencion < this.intervaloMax ?
                super.bonificarIntervalo((long) ((double) (intervaloAnterior) * coeficienteDeRetencion), dificultad)
                : this.intervaloMax;
        return intervaloActual;
    }

    @Override
    public Long estimarIntervalo(Long intervaloAnterior, Integer dificultad) {
        return 0L;
    }

    @Override
    public void actualizarEstado() {
        super.getMemoCard().cambiarEstado(new Reaprendizaje(
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
