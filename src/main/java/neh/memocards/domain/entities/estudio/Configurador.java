package neh.memocards.domain.entities.estudio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Setter @Getter
@AllArgsConstructor
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

    public void Configurador() {
        this.intervaloInicial = new ArrayList<Long>();
    }

    public static Configurador configuradorPredeterminado() {
        return new Configurador(
                List.of(15L, 1440L, 4320L),
                2.5f,
                1.3f,
                1.2f,
                1440L,
                345600L,
                8
        );
    }

}
