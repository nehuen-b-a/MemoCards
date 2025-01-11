package neh.memocards.domain.entities.estudio;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Setter @Getter
public class Configurador {
    // Atributos
    private List<Long> intervaloInicial;
    private Float factorFacilidad;
    private Float bonusFacil;
    private Float intervaloDificil;
    private Long intervaloMinimo;
    private Long intervaloMaximo;
    private Integer umbralSanguijuelas;

    // Métodos
    public Map<String, Object> obtenerParametros() {
        // Implementación pendiente
        return null;
    }

    public void actualizarParametro(String nombreParametro, Object nuevoValor) {
        // Implementación pendiente
    }
}
