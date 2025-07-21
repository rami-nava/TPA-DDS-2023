package models.domain.Notificaciones;


public class ViaMail implements MedioDeComunicacion{
  private AdapterViaMail servicioMail;
  public ViaMail(){
    this.servicioMail = new ViaMailJavax(); //acoplamiento con biblioteca externa
  }
  public void recibirNotificacion(String mensaje, String asunto, String destinatario) {
    this.servicioMail.recibirNotificacion(mensaje, destinatario, asunto);
  }
  public void recibirArchivos(String ruta, String asunto, String destinatario){
    this.servicioMail.enviarArchivo(ruta,destinatario,asunto);
  }

  public String nombre(){
    return "Mail";
  }
  
}