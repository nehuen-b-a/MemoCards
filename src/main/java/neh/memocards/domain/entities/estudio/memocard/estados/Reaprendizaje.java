package neh.memocards.domain.entities.estudio.memocard.estados;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Reaprendizaje extends EstadoMemoCard {
    // Atributos
    private Long umbralIntevaloMax;
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

        List<Long> intervaloInicial = memoCard.getConfigurador().getIntervaloInicial();
        this.umbralIntevaloMax = intervaloInicial.get(intervaloInicial.size() - 1);

        int posMayorAUnDia = IntStream.range(0, intervaloInicial.size()) // Creamos un flujo de índices
                .filter(i -> intervaloInicial.get(i) > 1440L)
                .findFirst()
                .orElse(-1);
        if(posMayorAUnDia == -1){
            this.intevaloMin = 1440L;
        }
        else{
            this.intevaloMin= intervaloInicial.get(posMayorAUnDia);
        }
        this.cantidadDeAciertos = 0;
    }
}
