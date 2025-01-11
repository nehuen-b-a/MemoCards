package neh.memocards.domain.entities.estudio.memocard;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RespuestaMemo {
    // Atributos
    private Long id;
    private String texto;
    private String rutaImagen;
    private String rutaAudio;
    private String rutaVideo;

    // Métodos (por implementar según necesidades)
}
