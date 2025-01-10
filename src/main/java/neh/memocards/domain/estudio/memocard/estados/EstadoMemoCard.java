package neh.memocards.domain.estudio.memocard.estados;

import domain.entities.estudio.memocard.MemoCard;

public abstract class EstadoMemoCard {
    // Atributos
    private Long id;
    private String nombre;
    private MemoCard carta;

    // Métodos
    public abstract void calcularIntervalo(Integer intervalo, Integer dificultad);

    public abstract void actualizarEstado(EstadoMemoCard nuevoEstado);
}