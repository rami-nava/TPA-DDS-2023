package models.domain.Notificaciones.Reglas;

import models.domain.Incidentes.ReporteDeIncidente;

public interface ReglaNotificacion {
  boolean cumpleRegla(ReporteDeIncidente reporteDeIncidente);
}
