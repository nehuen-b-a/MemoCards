package neh.memocards.domain.estudio.memocard.estados;

import domain.entities.estudio.memocard.MemoCard;

import java.time.LocalDateTime;

public class HistorialEstadoMemoCard {
    // Atributos
    private Long id;
    private MemoCard memoCard;
    private EstadoMemoCard estadoAnterior;
    private EstadoMemoCard estadoNuevo;
    private LocalDateTime fechaCambio;

    // Métodos (por implementar según necesidades)
}
