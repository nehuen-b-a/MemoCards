package neh.memocards.domain.entities.estudio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Entity @Table(name = "sesiones_estudio")
public class SesionDeEstudio {
    // Atributos
    @Setter
    @Id
    @GeneratedValue
    Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name="estudiante_id", referencedColumnName = "id")
    Estudiante estudiante;

    @Setter
    @OneToOne
    @JoinColumn(name="mazo_id", referencedColumnName = "id")
    Mazo mazo;

    @Setter
    @Column(name = "tiempo_estudio")
    Long tiempoEstudio;

    @ManyToMany
    @JoinTable(
            name = "memocards_por_revisar",
            joinColumns = @JoinColumn(name = "sesion_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "memo_id", referencedColumnName = "id")
    )
    Set<MemoCard> memoCardsPorRevisar;

    @ManyToMany
    @JoinTable(
            name = "memocards_revisadas",
            joinColumns = @JoinColumn(name = "sesion_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "memo_id", referencedColumnName = "id")
    )
    Set<MemoCard> memoCardsRevisadas;

    @Setter
    @Column(name="cantidad_total_intentos")
    Integer cantidadTotalIntentos;

    @Setter
    @Column(name="cantidad_total_aciertos")
    Integer cantidadTotalAciertos;

    @Setter
    @Column(name="cantidad_total_desaciertos")
    Integer cantidadTotalDesaciertos;

    @Setter
    @Column(name="cantidad_total_dificil")
    Integer cantidadTotalDificil;

    @Setter
    @Column(name="cantidad_total_bien")
    Integer cantidadTotalBien;

    @Setter
    @Column(name="cantidad_total_facil")
    Integer cantidadTotalFacil;

    @Setter
    @Column(name="fecha_inicio")
    LocalDateTime fechaInicio;

    @Setter
    @Column(name="fecha_fin")
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
        this();
        this.estudiante = estudiante;
        this.mazo = mazo;
        this.memoCardsPorRevisar = memoCardsPorRevisar;
    }

    public void comenzarSesion() {
        this.fechaInicio = LocalDateTime.now();
        memoCardsPorRevisar.forEach(memo -> memo.setSesionDeEstudioActual(this));
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

        if (memoCardsRevisadas != null) {
            memoCardsRevisadas.forEach(memo -> memo.setSesionDeEstudioActual(null));
        }

        memoCardsPorRevisar.forEach(memo -> memo.setSesionDeEstudioActual(null));

    }

    public void registrarMetrica(MemoCard memoCard, Dificultad dificultad) {
        // Registro la MemoCard en las revisadas y aumento Aciertos o Desaciertos

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
        if(memoCard.getEstadoMemoCard().getIntervaloActual() >= mazo.getPreferencia().getIntervaloMaximoARevisarEnUnaSesion()) {
            memoCardsRevisadas.add(memoCard);
            memoCardsPorRevisar.remove(memoCard);
            mazo.repasarMemoCard(memoCard);
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

    public boolean estaActiva() {
        return this.fechaInicio != null && this.fechaFin == null;
    }

    public void estudiarMemoCard(MemoCard memoCardActual, Dificultad dificultad) {
        memoCardActual.getEstadoMemoCard().cambiarIntervalo(memoCardActual.getIntervaloMinutos(),dificultad);
        revisarMemoCard(memoCardActual);
    }
}
