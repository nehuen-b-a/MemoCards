package neh.memocards.domain.entities.estudio.memocard;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.Configurador;
import neh.memocards.domain.entities.estudio.SesionDeEstudio;
import neh.memocards.domain.entities.estudio.memocard.estados.Aprendizaje;
import neh.memocards.domain.entities.estudio.memocard.estados.EstadoMemoCard;
import neh.memocards.domain.entities.estudio.memocard.estados.HistorialEstadoMemoCard;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Entity @Table(name="memocards")
public class MemoCard {
    // Atributos
    @Setter
    @Id @GeneratedValue
    private Long id;

    @Setter
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Setter
    @Column(name = "pregunta", nullable = false)
    private String pregunta;

    @Setter
    @OneToOne @JoinColumn(name = "rta_id", referencedColumnName = "id")
    private RespuestaMemo respuesta;

    @Setter
    private LocalDateTime fechaUltimoRepaso;

    @Setter
    private EstadoMemoCard estadoMemoCard;

    @Setter

    private Configurador configurador;

    private List<HistorialEstadoMemoCard> historialEstados;

    @Setter
    private Long intervaloMinutos;
    @Setter
    private Integer cantidadDeOlvidos;
    @Setter
    private Integer intentos;
    @Setter
    private Boolean esSanguijuela;
    @Setter
    private boolean esNueva;
    @Setter
    private SesionDeEstudio sesionDeEstudioActual;


    // Métodos

    public MemoCard() {
        historialEstados = new ArrayList<>();
        this.intervaloMinutos = 0L;
        this.cantidadDeOlvidos = 0;
        this.intentos = 0;
        this.esSanguijuela = false;
        this.esNueva = true;
    }

    public void cambiarEstado(EstadoMemoCard nuevoEstado) {
        HistorialEstadoMemoCard nuevoRegistroDeEstado = new HistorialEstadoMemoCard(this, estadoMemoCard, nuevoEstado);
        historialEstados.add(nuevoRegistroDeEstado);
        estadoMemoCard = nuevoEstado;
    }


    public void actualizarSaguijela() {
        this.cantidadDeOlvidos = getEstadoMemoCard().getRachaDesaciertos();
        this.esSanguijuela = cantidadDeOlvidos >= 8;
    }


    public void reiniciarTarjeta() {
        this.intervaloMinutos = 0L;
        this.cantidadDeOlvidos = 0;
        this.intentos = 0;
        this.esSanguijuela = false;
        Aprendizaje nuevoEstado = new Aprendizaje(this);
        cambiarEstado(nuevoEstado);
    }

    public Boolean estaListaParaRepasar(){
        return fechaProximoRepaso().isBefore(LocalDateTime.now());
    }

    public LocalDateTime fechaProximoRepaso(){
        return  fechaUltimoRepaso.plusMinutes(intervaloMinutos);
    }

    public boolean esNueva() {
        return this.esNueva;
    }
}

