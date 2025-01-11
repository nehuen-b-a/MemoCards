package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;

public class Repaso extends EstadoMemoCard {
    // Atributos
    private Float coeficienteDeRetencion;
    private Float coeficienteDeBonusPorFacilidad;
    private Float coeficientePorDificultad;
    private Long intervaloMax;

    // Métodos
    @Override
    public void calcularIntervalo(Integer intervalo, Integer dificultad) {
        // Implementación pendiente
    }

    @Override
    public void actualizarEstado(EstadoMemoCard nuevoEstado) {
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
