package neh.memocards.domain.entities.estudio.memocard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
public class RespuestaMemo {
    // Atributos
    private Long id;
    private String texto;
    private String rutaImagen;
    private String rutaAudio;
    private String rutaVideo;

    // Métodos (por implementar según necesidades)

    public RespuestaMemo(String texto) {
        new RespuestaMemo();
        this.texto = texto;
    }
}
