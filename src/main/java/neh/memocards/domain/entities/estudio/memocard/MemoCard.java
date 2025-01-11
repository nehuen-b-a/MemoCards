package neh.memocards.domain.entities.estudio.memocard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.memocard.estados.Aprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.EstadoMemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.HistorialEstadoMemoCard;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor

public class MemoCard {
    // Atributos
    @Setter
    private Long id;
    @Setter
    private String nombre;
    @Setter
    private String pregunta;
    @Setter
    private RespuestaMemo respuesta;
    @Setter
    private LocalDateTime fechaUltimoRepaso;
    @Setter
    private EstadoMemoCard estadoAprendizaje;
    @Setter
    private Configurador configurador;

    private List<HistorialEstadoMemoCard> historialEstados;

    @Setter
    private Long intervaloMinutos;
    @Setter
    private Integer cantidadDeOlvidos;
    @Setter
    private Boolean esSanguijuela;

    // Métodos

    public MemoCard(){
        historialEstados = new ArrayList<>();
    }

    public void cambiarEstado(EstadoMemoCard nuevoEstado) {
        HistorialEstadoMemoCard nuevoRegistroDeEstado  = new HistorialEstadoMemoCard(this, estadoAprendizaje, nuevoEstado);
        historialEstados.add(nuevoRegistroDeEstado);
        estadoAprendizaje = nuevoEstado;
    }


    public void reiniciarTarjeta() { Aprendizaje nuevoEstado = new Aprendizaje(this); cambiarEstado(nuevoEstado);}
}

