package models.domain.Notificaciones;

import models.domain.Incidentes.ReporteDeIncidente;
import models.persistence.Persistente;
import models.persistence.Converters.FormaDeNotificarAttributeConverter;
import models.persistence.Converters.MedioDeComunicacionAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "receptorDeNotificaciones")
public class ReceptorDeNotificaciones extends Persistente {
  @Convert(converter = FormaDeNotificarAttributeConverter.class)
  @Column(name = "formaDeNotificar")
  private FormaDeNotificar formaDeNotificar;
  @Convert(converter = MedioDeComunicacionAttributeConverter.class)
  @Column(name = "medioDeComunicacion")
  private MedioDeComunicacion medioDeComunicacion;
  @Setter
  @Column(name = "mail")
  private String mail;
  @Setter
  @Column(name = "telefono")
  private String telefono;

  public ReceptorDeNotificaciones() {}
  private String getDestinatario(){
    String destinatario = null;

    switch (this.medioDeComunicacion.getClass().getSimpleName()){
      case "ViaMail" : destinatario = this.mail; break;
      case "ViaWPP" : destinatario = this.telefono; break;
    }
    return destinatario;
  }
  public void recibirNotificacion(ReporteDeIncidente reporteDeIncidente){
    this.formaDeNotificar.recibirNotificacion(this.medioDeComunicacion, reporteDeIncidente, this.getDestinatario());
  }
  public void recibirSolicitudDeRevision(ReporteDeIncidente reporteDeIncidente) {
    this.medioDeComunicacion.recibirNotificacion(reporteDeIncidente.mensajeRevision(), "Solicitud de Revisión de Incidente", this.getDestinatario());
  }

  public void cambiarFormaDeNotificar(FormaDeNotificar forma) {
    if((this.formaDeNotificar != null) && (this.formaDeNotificar.getClass().getSimpleName().equals("SinApuros")))
        this.envioProgramado(); //Se envian los mensajes que estaban programados
    this.formaDeNotificar = forma;
  }
  public void cambiarMedioDeComunicacion(MedioDeComunicacion medio) {
    this.medioDeComunicacion = medio;
  }

  public void envioProgramado(){
    this.formaDeNotificar.envioProgramado(this.medioDeComunicacion, this.getDestinatario());
  }
}
