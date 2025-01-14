package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

@Setter
@Getter
public abstract class EstadoMemoCard {
    // Atributos
    private Long id;
    private String nombre;
    protected MemoCard memoCard;

    protected Long intervaloActual;
    protected Integer intentos;
    protected Integer cantidadDeAciertos;
    protected Integer cantidadDeDesaciertos;

    private Integer umbralSanguijuela;
    private Double coeficienteDeBonusPorFacilidad;
    private Double coeficientePorDificultad;


    // Métodos
    public abstract Long calcularIntervalo(Long intervaloAnterior, Integer dificultad);

    public abstract void actualizarEstado();

    public EstadoMemoCard(MemoCard memoCard, String nombre) {
        this.nombre = nombre;
        this.memoCard = memoCard;

        this.intentos = 0;
        this.cantidadDeDesaciertos = 0;

        Configurador configurador = memoCard.getConfigurador();
        this.umbralSanguijuela = configurador.getUmbralSanguijuelas();
        this.coeficienteDeBonusPorFacilidad = configurador.getBonusFacil();
        this.coeficientePorDificultad = configurador.getIntervaloDificil();
    }


    protected Long bonificarIntervalo(Long intervalo, Integer dificultad) {
        Double bonificacion = switch (dificultad) {
            case 0 -> this.coeficienteDeBonusPorFacilidad;
            case 2 -> this.coeficientePorDificultad;
            default -> 1d;
        };
        return (Long) (long) (intervalo * bonificacion);
    }

    public void actualizarConfiguracion() {
        Configurador configurador = memoCard.getConfigurador();
        coeficienteDeBonusPorFacilidad = configurador.getBonusFacil();
        coeficientePorDificultad = configurador.getIntervaloDificil();
        umbralSanguijuela = configurador.getUmbralSanguijuelas();
    }

}