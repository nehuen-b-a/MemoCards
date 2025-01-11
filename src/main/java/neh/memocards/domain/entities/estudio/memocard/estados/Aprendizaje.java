package neh.memocards.domain.entities.estudio.memocard.estados;

import lombok.AllArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.util.List;


public class Aprendizaje extends EstadoMemoCard {
    // Atributos

    private List<Long> intervalos;
    private Integer cantidadDeAciertos;

    // Métodos

    public Aprendizaje(MemoCard memoCard) {
        super(memoCard,"APRENDIZAJE");
        this.intervalos = memoCard.getConfigurador().getIntervaloInicial();
        this.cantidadDeAciertos = 0;
    }

    @Override
    public void calcularIntervalo(Integer intervalo, Integer dificultad) {
        this.cantidadDeAciertos =  dificultad <= 2 ? cantidadDeAciertos + 1: 0;
        if(this.intervalos.get(cantidadDeAciertos) >= intervalos.get(cantidadDeAciertos -1)){
            super.getMemoCard().cambiarEstado(new Repaso(super.getMemoCard()));
        }
    }

    @Override
    public void actualizarEstado(EstadoMemoCard nuevoEstado) {
        // Implementación pendiente
    }


}
