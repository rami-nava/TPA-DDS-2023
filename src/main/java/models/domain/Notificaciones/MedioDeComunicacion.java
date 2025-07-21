package models.domain.Notificaciones;

public interface MedioDeComunicacion {
  void recibirNotificacion(String mensaje, String asunto, String destinatario);
  String nombre();
}
