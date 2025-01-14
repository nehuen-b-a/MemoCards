package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;


public class Repaso extends EstadoMemoCard {
    // Atributos
    private Double coeficienteDeRetencion;
    private Long intervaloMax;

    // Métodos
    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        if (dificultad >= 3) {
            this.actualizarEstado();
            return super.getMemoCard().getEstadoAprendizaje().calcularIntervalo(intervaloAnterior, dificultad);
        }
        this.cantidadDeAciertos++;
        this.intervaloActual = (double) (intervaloAnterior) * coeficienteDeRetencion < this.intervaloMax ?
                super.bonificarIntervalo((long) ((double) (intervaloAnterior) * coeficienteDeRetencion), dificultad)
                : this.intervaloMax;
        return intervaloActual;
    }

    @Override
    public void actualizarEstado() {
        super.getMemoCard().cambiarEstado(new Reaprendizaje(
                super.getMemoCard(),
                this.intervaloActual,
                this.intentos,
                this.cantidadDeAciertos,
                this.cantidadDeDesaciertos));
    }

    public Repaso(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.coeficienteDeRetencion = memoCard.getConfigurador().getFactorFacilidad();
        this.intervaloMax = memoCard.getConfigurador().getIntervaloMaximo();
    }

    public Repaso(MemoCard memoCard, Long intervaloActual, Integer intentos, Integer cantidadDeAciertos, Integer cantidadDeDesaciertos) {
        this(memoCard); // Llama al constructor base
        this.intervaloActual = intervaloActual;
        this.intentos = intentos;
        this.cantidadDeAciertos = cantidadDeAciertos;
        this.cantidadDeDesaciertos = cantidadDeDesaciertos;
    }

    @Override
    public void actualizarConfiguracion() {
        super.actualizarConfiguracion();
        Configurador configurador = this.memoCard.getConfigurador();
        this.coeficienteDeRetencion = configurador.getFactorFacilidad();
        this.intervaloMax = configurador.getIntervaloMaximo();
    }

}
