package neh.memocards.domain.entities.estudio.memocard.estados;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "estados_memocard")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="estado")
public abstract class EstadoMemoCard {
    // Atributos
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @OneToOne
    @JoinColumn(name ="memo_id", referencedColumnName = "id", nullable = false)
    protected MemoCard memoCard;

    @Column(name = "intervalo_actual", nullable = false)
    protected Long intervaloActual;

    @Column(name = "racha_aciertos")
    protected Integer rachaAciertos;

    @Column(name = "racha_desaciertos")
    protected Integer rachaDesaciertos;

    @Column(name = "umbral_sanguijuela", nullable = false)
    private Integer umbralSanguijuela;

    @Column(name = "coeficiente_bonus_facilidad",nullable = false)
    private Double coeficienteDeBonusPorFacilidad;

    @Column(name = "coeficiente_dificultad", nullable = false)
    private Double coeficientePorDificultad;


    // Métodos
    public abstract Long cambiarIntervalo(Long intervaloAnterior, Dificultad dificultad);

    public abstract Long estimarIntervalo(Long intervaloAnterior, Dificultad dificultad);

    public abstract void actualizarEstado();

    public EstadoMemoCard(MemoCard memoCard, String nombre) {
        this.nombre = nombre;
        this.memoCard = memoCard;

        this.memoCard.setIntentos(0);
        this.rachaDesaciertos = 0;

        Configurador configurador = memoCard.getConfigurador();
        this.umbralSanguijuela = configurador.getUmbralSanguijuelas();
        this.coeficienteDeBonusPorFacilidad = configurador.getBonusFacil();
        this.coeficientePorDificultad = configurador.getBonusDificil();
    }


    protected Long bonificarIntervalo(Long intervalo, Dificultad dificultad) {
        Double bonificacion = switch (dificultad) {
            case FACIL -> this.coeficienteDeBonusPorFacilidad;
            case DIFICIL -> this.coeficientePorDificultad;
            default -> 1d;
        };
        return (Long) (long) (intervalo * bonificacion);
    }

    public void actualizarConfiguracion() {
        Configurador configurador = memoCard.getConfigurador();
        coeficienteDeBonusPorFacilidad = configurador.getBonusFacil();
        coeficientePorDificultad = configurador.getBonusDificil();
        umbralSanguijuela = configurador.getUmbralSanguijuelas();
    }

    protected void actualizarSesion(Dificultad dificultad) {
        memoCard.getSesionDeEstudioActual().registrarMetrica(memoCard, dificultad);
        memoCard.setEsNueva(false);
    }


}