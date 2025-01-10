package neh.memocards.domain.entities.validador;


public interface TipoValidacion {
  boolean validar(String clave);
  String getMensajeError();
}

