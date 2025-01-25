package neh.memocards.domain.entities.estudio.memocard.estados;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="historial_estados_memocard")
public class HistorialEstadoMemoCard {
    // Atributos
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memo_id", referencedColumnName = "id")
    private MemoCard memoCard;

    @OneToOne
    @JoinColumn(name = "estado_anterior_id", referencedColumnName = "id")
    private EstadoMemoCard estadoAnterior;

    @OneToOne
    @JoinColumn(name = "estado_nuevo_id", referencedColumnName = "id", nullable = false)
    private EstadoMemoCard estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    // Métodos
    public HistorialEstadoMemoCard( MemoCard memoCard, EstadoMemoCard estadoAnterior, EstadoMemoCard estadoNuevo) {
        this.memoCard = memoCard;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = LocalDateTime.now();
    }

}
