package models.domain.Seguridad;

public class NoSeEncontroElArchivo extends  RuntimeException{

    public NoSeEncontroElArchivo(String mensaje) {
      super(mensaje);
    }
  }

