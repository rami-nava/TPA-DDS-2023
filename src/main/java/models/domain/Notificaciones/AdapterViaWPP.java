package models.domain.Notificaciones;

public interface AdapterViaWPP {
  void recibirNotificacion(String mensaje, String telefonoDestinatario, String asunto);
}
