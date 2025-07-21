package models.domain.Notificaciones;

import models.domain.Incidentes.ReporteDeIncidente;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeNotificaciones;

import javax.persistence.EntityManager;
import java.util.List;

public class SinApuros extends FormaDeNotificar{
  private NotificacionDeIncidente resumenDeIncidentes;
  private RepositorioDeNotificaciones repositorioDeNotificaciones;
  private EntityManager em;
  public SinApuros() {
    this.repositorioDeNotificaciones = RepositorioDeNotificaciones.getInstancia();
  }
  @Override
  public void recibirNotificacion(MedioDeComunicacion medioDeComunicacion, ReporteDeIncidente reporteDeIncidente, String destinatario) {
    List<NotificacionDeIncidente> notificaciones = this.repositorioDeNotificaciones.buscarTodos();

    if (this.superif(notificaciones, reporteDeIncidente, destinatario)) {
      NotificacionDeIncidente notificacionDeIncidente = new NotificacionDeIncidente();
      notificacionDeIncidente.setReporteDeIncidente(reporteDeIncidente);
      notificacionDeIncidente.setDestinatario(destinatario);
      //EntityManager em = EntityManagerSingleton.getInstance();
      //em.getTransaction().begin();
      this.repositorioDeNotificaciones.agregar(notificacionDeIncidente);
      //em.getTransaction().commit();
    }
  }

  public boolean superif(List<NotificacionDeIncidente> notificaciones, ReporteDeIncidente reporteDeIncidente, String destinatario) {
    return !notificaciones.stream().anyMatch(n -> !n.getEnviada() && n.getReporteDeIncidente().igualito(reporteDeIncidente) && n.getReporteDeIncidente().getClasificacion().equals(reporteDeIncidente.getClasificacion()) && n.getDestinatario().equals(destinatario));
  }
  @Override
  public void envioProgramado(MedioDeComunicacion medioDeComunicacion, String destinatario) {
    this.repositorioDeNotificaciones.buscarTodos().stream().filter(n -> !n.getEnviada() && n.getDestinatario().equals(destinatario) ).toList()
        .forEach(n -> this.notificarReporte(medioDeComunicacion, n));
  }
  private void notificarReporte(MedioDeComunicacion medioDeComunicacion, NotificacionDeIncidente notificacionDeIncidente) {
    super.recibirNotificacion(medioDeComunicacion, notificacionDeIncidente.getReporteDeIncidente(), notificacionDeIncidente.getDestinatario());
    notificacionDeIncidente.setEnviada(true);
    this.repositorioDeNotificaciones.agregar(notificacionDeIncidente);
  }

  @Override
  public String nombre(){
    return "Sin Apuros";
  }
}
