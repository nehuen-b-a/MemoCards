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
    public SesionDeEstudio(List<MemoCard> memoCardsArevisar,Estudiante estudiante, Mazo mazo) {
        new SesionDeEstudio();
        this.estudiante = estudiante;
        this.mazo = mazo;
    }

    public void comenzarSesion() {
        this.fechaInicio = LocalDateTime.now();
        memoCardsRevisadas.forEach(memo -> memo.setSesionDeEstudioActual(this));
    }

    public void finalizarSesion() {
        this.fechaFin = LocalDateTime.now();
        this.estaFinalizada = true;


        if (this.fechaInicio != null && this.fechaFin != null) {
            this.tiempoEstudio = java.time.Duration.between(this.fechaInicio, this.fechaFin).toSeconds();
        }


        memoCardsRevisadas.forEach(memo -> memo.setSesionDeEstudioActual(null));
    }

    public void registrarMetrica(MemoCard memoCard, Dificultad dificultad) {

        this.memoCardsRevisadas.add(memoCard);

        switch (dificultad) {
            case OLVIDO:
                this.cantidadTotalDesaciertos++;
                break;
            case DIFICIL:
                this.cantidadTotalDificil++;
                this.cantidadTotalAciertos++;
                break;
            case BIEN:
                this.cantidadTotalBien++;
                this.cantidadTotalAciertos++;
                break;
            case FACIL:
                this.cantidadTotalFacil++;
                this.cantidadTotalAciertos++;
                break;
            default:
                throw new IllegalArgumentException("Dificultad no válida: " + dificultad);
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
