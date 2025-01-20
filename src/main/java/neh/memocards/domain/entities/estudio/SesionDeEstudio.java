package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class SesionDeEstudio {
    // Atributos
    @Setter
    Long id;
    @Setter
    Estudiante estudiante;
    @Setter
    Mazo mazo;
    @Setter
    Long tiempoEstudio;
    Set<MemoCard> memoCardsPorRevisar;
    Set<MemoCard> memoCardsRevisadas;
    @Setter
    Integer cantidadTotalIntentos;
    @Setter
    Integer cantidadTotalAciertos;
    @Setter
    Integer cantidadTotalDesaciertos;
    @Setter
    Integer cantidadTotalDificil;
    @Setter
    Integer cantidadTotalBien;
    @Setter
    Integer cantidadTotalFacil;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    @Setter
    Boolean estaFinalizada;

    // Métodos

    public SesionDeEstudio() {
        this.tiempoEstudio = 0L;
        this.memoCardsRevisadas = new HashSet<>();
        this.cantidadTotalIntentos = 0;
        this.cantidadTotalAciertos = 0;
        this.cantidadTotalDesaciertos = 0;
        this.cantidadTotalDificil = 0;
        this.cantidadTotalBien = 0;
        this.cantidadTotalFacil = 0;
        this.fechaInicio = null;
        this.fechaFin = null;
        this.estaFinalizada = false;
    }

    public SesionDeEstudio(Set<MemoCard> memoCardsPorRevisar, Estudiante estudiante, Mazo mazo) {
        new SesionDeEstudio();
        this.estudiante = estudiante;
        this.mazo = mazo;
        this.memoCardsPorRevisar = memoCardsPorRevisar;
    }

    public void comenzarSesion() {
        this.fechaInicio = LocalDateTime.now();
        memoCardsRevisadas.forEach(memo -> memo.setSesionDeEstudioActual(this));
    }

    public void finalizarSesion() {
        if (this.fechaInicio == null) {
            throw new NullPointerException();
        }
        this.fechaFin = LocalDateTime.now();
        this.estaFinalizada = true;

        // Calculo el tiempo total de estudio

        this.tiempoEstudio = java.time.Duration.between(this.fechaInicio, this.fechaFin).toSeconds();

        // Finalizo sesión para todas las MemoCards
        memoCardsRevisadas.forEach(memo -> memo.setSesionDeEstudioActual(null));
    }

    public void registrarMetrica(MemoCard memoCard, Dificultad dificultad) {
        // Registro la MemoCard en las revisadas y aumento Aciertos o Desaciertos
        this.memoCardsRevisadas.add(memoCard);

        switch (dificultad) {
            case OLVIDO:
                this.cantidadTotalDesaciertos++;
                break;
            case DIFICIL:
                this.cantidadTotalDificil++;
                revisarMemoCard(memoCard);
                break;
            case BIEN:
                this.cantidadTotalBien++;
                revisarMemoCard(memoCard);
                break;
            case FACIL:
                this.cantidadTotalFacil++;
                revisarMemoCard(memoCard);
                break;
            default:
                throw new IllegalArgumentException("Dificultad no válida: " + dificultad);
        }
    }

    private void revisarMemoCard(MemoCard memoCard) {
        this.cantidadTotalAciertos++;
        if(memoCard.getIntervaloMinutos() >= mazo.getPreferencia().getIntervaloMaximoARevisarEnUnaSesion()) {
            memoCardsRevisadas.add(memoCard);
            memoCardsPorRevisar.remove(memoCard);
        }
    }

    public Map<String, Object> obtenerMetrica() {
        Map<String, Object> metricas = new HashMap<>();
        metricas.put("cantidadTotalIntentos", this.cantidadTotalIntentos);
        metricas.put("cantidadTotalAciertos", this.cantidadTotalAciertos);
        metricas.put("cantidadTotalDesaciertos", this.cantidadTotalDesaciertos);
        metricas.put("cantidadTotalDificil", this.cantidadTotalDificil);
        metricas.put("cantidadTotalBien", this.cantidadTotalBien);
        metricas.put("cantidadTotalFacil", this.cantidadTotalFacil);
        metricas.put("tiempoEstudio", this.tiempoEstudio);
        metricas.put("cantidadMemoCardsRevisadas", this.memoCardsRevisadas.size());
        return metricas;
    }

}
