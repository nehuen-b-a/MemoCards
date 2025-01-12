package neh.memocards.domain.entities.estudio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Configurador {
    // Atributos

    private List<Long> intervaloInicial;
    @Setter
    private Double factorFacilidad;
    @Setter
    private Double bonusFacil;
    @Setter
    private Double intervaloDificil;
    @Setter
    private Long intervaloMinimo;
    @Setter
    private Long intervaloMaximo;
    @Setter
    private Integer umbralSanguijuelas;

    // Métodos


     public void setIntervaloInicial(List<Long>  intervaloInicial) {
         if(intervaloInicial == null || intervaloInicial.isEmpty()) {
             throw new IllegalArgumentException();
         }
         this.intervaloInicial = intervaloInicial.stream().sorted((a, b) -> Long.compare(b, a)).collect(Collectors.toList());
     }

     public void Configurador() {
        this.intervaloInicial = new ArrayList<Long>();
    }

    public static Configurador configuradorPredeterminado() {
        return new Configurador(
                List.of(15L, 1440L, 4320L),
                2.5d,
                1.3d,
                0.8333d,
                1440L,
                345600L,
                8
        );
    }

}
