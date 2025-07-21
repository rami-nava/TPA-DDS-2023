package models.services.Archivos;

public class NoSePudoLeerArchivoCSV extends RuntimeException{
  public NoSePudoLeerArchivoCSV(String mensaje) {
    super(mensaje);
  }
}
