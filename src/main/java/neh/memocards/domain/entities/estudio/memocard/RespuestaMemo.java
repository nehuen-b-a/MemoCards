package neh.memocards.domain.entities.estudio.memocard;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@Entity @Table(name = "respuestas")
public class RespuestaMemo {
    // Atributos
    @Id @GeneratedValue
    private Long id;

    @Column(name = "texto", nullable = false)
    private String texto;

    @Column(name = "rutaImagen", nullable = false)
    private String rutaImagen;

    @Column(name = "rutaAudio", nullable = false)
    private String rutaAudio;

    @Column(name = "rutaVideo", nullable = false)
    private String rutaVideo;

    // Métodos (por implementar según necesidades)

    public RespuestaMemo(String texto) {
        new RespuestaMemo();
        this.texto = texto;
    }
}
