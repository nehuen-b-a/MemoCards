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

    @Column(name = "respuesta-markdonw", nullable = false)
    private String respuestaMarkdonw;

    // Métodos (por implementar según necesidades)

    public RespuestaMemo(String texto) {
        new RespuestaMemo();
        this.respuestaMarkdonw = texto;
    }
}
