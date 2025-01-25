package neh.memocards.domain.entities.estudio;

import jakarta.persistence.*;
import lombok.*;
import neh.memocards.domain.converters.TipoBarajadorConverter;
import neh.memocards.domain.entities.estudio.memocard.barajador.ITipoBarajador;
import neh.memocards.domain.entities.estudio.memocard.barajador.IntervaloMenorAMayor;


import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "configuraciones")

public class Configurador {
    // Atributos
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "estudiante_id", referencedColumnName = "id")
    private Estudiante estudiante;

    @ElementCollection
    @CollectionTable(
            name = "config_intervalos_iniciales",
            joinColumns = @JoinColumn(name = "config_id")
    )
    @Column(name = "intervalo")
    private List<Long> intervaloInicial;

    @Setter
    @Column(name = "distancia_porcentual_intervalo", nullable = false)
    private Double distanciaPorcentualIntervalo;

    @Setter
    @Column(name = "coeficiente_de_retencion", nullable = false)
    private Double coeficienteDeRetencion;

    @Setter
    @Column(name = "bonus_facil", nullable = false)
    private Double bonusFacil;

    @Setter
    @Column(name = "bonus_dificil", nullable = false)
    private Double bonusDificil;

    @Setter
    @Column(name = "intervalo_min", nullable = false)
    private Long intervaloMinimo;

    @Setter
    @Column(name = "intervalo_max", nullable = false)
    private Long intervaloMaximo;

    @Setter
    @Column(name = "umbral_sanguijuelas", nullable = false)
    private Integer umbralSanguijuelas;

    @Setter
    @Column(name = "maximo_de_nuevas_cartas", nullable = false)
    private Integer maximoDeNuevasCartas;

    @Setter
    @Column(name = "maximo_de_cartas_a_repasar", nullable = false)
    private Integer maximoDeCartasARepasar;

    @Setter
    @Column(name = "intervalo_max_X_sesion", nullable = false)
    private Long intervaloMaximoARevisarEnUnaSesion;

    @Setter
    @Column(name = "intervalo_inclusion_nuevas_memocards", nullable = false)
    private Long intervaloParaIncluirMemoCardsNuevas;

    @Setter
    @Convert(converter = TipoBarajadorConverter.class)
    private ITipoBarajador barajador;


    // Métodos

    public void setIntervaloInicial(List<Long> intervaloInicial) {
        if (intervaloInicial == null || intervaloInicial.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.intervaloInicial = intervaloInicial.stream().sorted((a, b) -> Long.compare(b, a)).collect(Collectors.toList());
    }

    public Configurador(List<Long> intervaloInicial,
                        Double distanciaPorcentualIntervalo,
                        Double coeficienteDeRetencion,
                        Double bonusFacil,
                        Double bonusDificil,
                        Long intervaloMinimo,
                        Long intervaloMaximo,
                        Integer umbralSanguijuelas,
                        Integer maximoDeNuevasCartas,
                        Integer maximoDeCartasARepasar,
                        Long intervaloMaximoARevisarEnUnaSesion,
                        Long intervaloParaIncluirMemoCardsNuevas,
                        ITipoBarajador barajador) {
        this.intervaloInicial = intervaloInicial;
        this.distanciaPorcentualIntervalo = distanciaPorcentualIntervalo;
        this.coeficienteDeRetencion = coeficienteDeRetencion;
        this.bonusFacil = bonusFacil;
        this.bonusDificil = bonusDificil;
        this.intervaloMinimo = intervaloMinimo;
        this.intervaloMaximo = intervaloMaximo;
        this.umbralSanguijuelas = umbralSanguijuelas;
        this.maximoDeNuevasCartas = maximoDeNuevasCartas;
        this.maximoDeCartasARepasar = maximoDeCartasARepasar;
        this.intervaloMaximoARevisarEnUnaSesion = intervaloMaximoARevisarEnUnaSesion;
        this.intervaloParaIncluirMemoCardsNuevas = intervaloParaIncluirMemoCardsNuevas;
        this.barajador = barajador;
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
                1440L,
                new IntervaloMenorAMayor()
        );
    }

}
