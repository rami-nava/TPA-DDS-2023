package models.domain.Notificaciones;


public interface AdapterViaMail {
  void recibirNotificacion(String mensaje, String mailDestinatario, String asunto);
  void enviarArchivo(String ruta, String mailDestinatario, String asunto);
}
