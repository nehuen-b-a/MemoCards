package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;


public class Repaso extends EstadoMemoCard {
    // Atributos
    private Double coeficienteDeRetencion;
    private Double coeficienteDeBonusPorFacilidad;
    private Double coeficientePorDificultad;
    private Long intervaloMax;

    // Métodos
    @Override
    public Long calcularIntervalo(Long intervalo, Integer dificultad) {

        return null;
    }

    @Override
    public void actualizarEstado() {
        // Implementación pendiente
    }

    public Repaso(MemoCard memoCard) {
        super(memoCard, "APRENDIZAJE");
        this.coeficienteDeRetencion = memoCard.getConfigurador().getFactorFacilidad();
        this.coeficienteDeBonusPorFacilidad = memoCard.getConfigurador().getFactorFacilidad();
        this.coeficientePorDificultad = memoCard.getConfigurador().getIntervaloDificil();
        this.intervaloMax = memoCard.getConfigurador().getIntervaloMaximo();
    }
}
