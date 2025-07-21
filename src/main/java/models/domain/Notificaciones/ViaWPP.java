package models.domain.Notificaciones;


public class ViaWPP implements MedioDeComunicacion{
  private AdapterViaWPP servicioWPP;
  public ViaWPP(){
    this.servicioWPP = new ViaWPPConcreto(); //acoplamiento
  }
  public void recibirNotificacion(String mensaje, String asunto, String destinatario) {
    this.servicioWPP.recibirNotificacion(mensaje, destinatario, asunto);
  }

  public String nombre(){
    return "Whatsapp";
  }
}
