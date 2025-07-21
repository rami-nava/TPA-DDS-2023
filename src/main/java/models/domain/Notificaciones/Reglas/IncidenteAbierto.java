package models.domain.Notificaciones.Reglas;

import models.domain.Incidentes.EstadoIncidente;
import models.domain.Incidentes.ReporteDeIncidente;

public class IncidenteAbierto implements ReglaNotificacion {
  @Override
  public boolean cumpleRegla(ReporteDeIncidente reporteDeIncidente) {
    return reporteDeIncidente.getClasificacion().equals(EstadoIncidente.ABIERTO);
  }
}
