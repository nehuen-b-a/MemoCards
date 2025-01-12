package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;


public class Repaso extends EstadoMemoCard {
    // Atributos
    private final Double coeficienteDeRetencion;
    private final Long intervaloMax;

    // Métodos
    @Override
    public Long calcularIntervalo(Long intervaloAnterior, Integer dificultad) {
        if(dificultad >= 3) {
            this.actualizarEstado();
            return super.getMemoCard().getEstadoAprendizaje().calcularIntervalo(intervaloAnterior, dificultad);
        }
        return (double)(intervaloAnterior) * coeficienteDeRetencion < this.intervaloMax?
                (long)((double)(intervaloAnterior) * coeficienteDeRetencion)
                : this.intervaloMax;
    }

    @Override
    public void actualizarEstado() {
        super.getMemoCard().cambiarEstado(new Reaprendizaje(super.getMemoCard()));
    }

    public Repaso(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.coeficienteDeRetencion = memoCard.getConfigurador().getFactorFacilidad();
        this.intervaloMax = memoCard.getConfigurador().getIntervaloMaximo();
    }
}
