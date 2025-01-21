package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.Dificultad;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

@Setter
@Getter
public abstract class EstadoMemoCard {
    // Atributos
    private Long id;
    private String nombre;
    protected MemoCard memoCard;

    protected Long intervaloActual;
    protected Integer rachaAciertos;
    protected Integer rachaDesaciertos;

    private Integer umbralSanguijuela;
    private Double coeficienteDeBonusPorFacilidad;
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