package neh.memocards.domain.entities.estudio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import neh.memocards.domain.entities.estudio.memocard.barajador.ITipoBarajador;
import neh.memocards.domain.entities.estudio.memocard.barajador.IntervaloMenorAMayor;


import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Configurador {
    // Atributos

    private List<Long> intervaloInicial;
    @Setter
    private Double distanciaPorcentualIntervalo;
    @Setter
    private Double coeficienteDeRetencion;
    @Setter
    private Double bonusFacil;
    @Setter
    private Double bonusDificil;
    @Setter
    private Long intervaloMinimo;
    @Setter
    private Long intervaloMaximo;
    @Setter
    private Integer umbralSanguijuelas;
    @Setter
    private Integer maximoDeNuevasCartas;
    @Setter
    private Integer maximoDeCartasARepasar;
    @Setter
    private Long intervaloMaximoARevisarEnUnaSesion;
    @Setter
    private ITipoBarajador barajador;




    // Métodos

    public void setIntervaloInicial(List<Long> intervaloInicial) {
        if (intervaloInicial == null || intervaloInicial.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.intervaloInicial = intervaloInicial.stream().sorted((a, b) -> Long.compare(b, a)).collect(Collectors.toList());
    }

    public static Configurador configuradorPredeterminado() {
        return new Configurador(
                List.of(15L, 1440L, 4320L),
                1.6,
                2.5d,
                1.3d,
                0.8333d,
                1440L,
                345600L,
                8,
                10,
                20,
                120L,
                new IntervaloMenorAMayor()
        );
    }

}
