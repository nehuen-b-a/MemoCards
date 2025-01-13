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
    private MemoCard memoCard;

    private Long intervaloActual;
    private Integer cantidadDesaciertos;
    private Integer umbralSanguijuela;

    private Double coeficienteDeBonusPorFacilidad;
    private Double coeficientePorDificultad;

    // Métodos
    public abstract Long calcularIntervalo(Long intervaloAnterior,Integer dificultad);

    public abstract void actualizarEstado();



    public EstadoMemoCard(MemoCard memoCard, String nombre) {
        this.nombre = nombre;
        this.memoCard = memoCard;

        this.cantidadDesaciertos = 0;

        Configurador configurador = memoCard.getConfigurador();
        this.umbralSanguijuela = configurador.getUmbralSanguijuelas();
        this.coeficienteDeBonusPorFacilidad = configurador.getBonusFacil();
        this.coeficientePorDificultad = configurador.getIntervaloDificil();
    }


    protected Long bonificarIntervalo(Long intervalo, Integer dificultad) {
        Double bonificacion;
        switch (dificultad) {
            case 0: bonificacion = this.coeficienteDeBonusPorFacilidad;
            break;
            case 2: bonificacion = this.coeficientePorDificultad;
            break;
            default: bonificacion = 1d;
        }
        System.out.println("Bonificador resultado: " + (Long)(long) (intervalo * bonificacion)  + ", Dificultad: " + dificultad);
        return (Long)(long) (intervalo * bonificacion);
    }


}