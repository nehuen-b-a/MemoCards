package neh.memocards.domain.estudio.memocard;

import domain.entities.estudio.Configurador;
import domain.entities.estudio.memocard.estados.EstadoMemoCard;
import domain.entities.estudio.memocard.estados.HistorialEstadoMemoCard;

import java.time.LocalDateTime;
import java.util.List;

public class MemoCard {
    // Atributos
    private Long id;
    private String nombre;
    private String pregunta;
    private RespuestaMemo respuesta;
    private LocalDateTime fechaUltimoRepaso;
    private EstadoMemoCard estadoAprendizaje;
    private List<HistorialEstadoMemoCard> historialEstados;
    private Long intervaloMinutos;
    private Integer cantidadDeOlvidos;
    private Boolean esSanguijuela;

    // Métodos
    public void cambiarEstado(EstadoMemoCard nuevoEstado, Configurador preferencias) {
        // Implementación pendiente
    }

    public void reiniciarTarjeta() {
        // Implementación pendiente
    }
}

