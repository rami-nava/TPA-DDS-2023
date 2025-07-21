package models.persistence.Repositorios;

import models.domain.Entidades.EntidadPrestadora;
import models.domain.Notificaciones.NotificacionDeIncidente;

public class RepositorioDeNotificaciones extends RepositorioGenerico<NotificacionDeIncidente> {
  private static RepositorioDeNotificaciones instancia = null;
  private RepositorioDeNotificaciones() {
    super(NotificacionDeIncidente.class);
  }
  public static  RepositorioDeNotificaciones getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioDeNotificaciones();
    }
    return instancia;
  }
}
