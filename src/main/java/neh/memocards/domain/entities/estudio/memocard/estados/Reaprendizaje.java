package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;
import java.util.stream.IntStream;

public class Reaprendizaje extends EstadoMemoCard {
    // Atributos
    private List<Long> intervalos;
    private Long intevaloMin;
    private Integer cantidadDeAciertos;

    // Métodos
    @Override
    public void calcularIntervalo(Integer intervalo, Integer dificultad) {
        // Implementación pendiente
    }

    @Override
    public void actualizarEstado(EstadoMemoCard nuevoEstado) {
        // Implementación pendiente
    }

    public Reaprendizaje(MemoCard memoCard) {
        super(memoCard,"REAPRENDIZAJE");

        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();

        int posMayorAUnDia = IntStream.range(0, this.intervalos.size()) // Creamos un flujo de índices
                .filter(i -> this.intervalos.get(i) > 1440L)
                .findFirst()
                .orElse(-1);
        if(posMayorAUnDia == -1){
            this.intevaloMin = 1440L;
        }
        else{
            this.intevaloMin= this.intervalos.get(posMayorAUnDia);
        }
        this.cantidadDeAciertos = 0;
    }
}
