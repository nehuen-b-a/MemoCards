package neh.memocards.domain.validador;


public interface TipoValidacion {
  boolean validar(String clave);
  String getMensajeError();
}

