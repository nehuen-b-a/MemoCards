package neh.memocards.domain.entities.estudio;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public class SesionDeEstudio {
    // Atributos
    private Long id;
    private Estudiante estudiante;
    private Mazo mazo;
    private Long tiempoEstudio;
    private Set<MemoCard> tarjetasRevisadas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean estaFinalizada;

    // Métodos
    public void iniciarSesion() {
        // Implementación pendiente
    }

    public void finalizarSesion() {
        // Implementación pendiente
    }

    public void registrarMetrica() {
        // Implementación pendiente
    }

    public Map<String, Object> obtenerMetrica() {
        // Implementación pendiente
        return null;
    }
}
