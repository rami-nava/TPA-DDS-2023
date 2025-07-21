package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Incidentes.Incidente;

@Setter
@Getter
public class SRPIncidente extends SRPGenerica {
  private String horaApertura; //Formato 'yyyy-MM-ddThh:mm:ss' ISO LOCAL DATE TIME
  private String horaCierre; //Formato 'yyyy-MM-ddThh:mm:ss' ISO LOCAL DATE TIME
  private SRPEstablecimiento establecimiento;
  private SRPServicio servicio;

  public SRPIncidente(Incidente incidente) {
    super(incidente);
    this.horaApertura = incidente.primeraApertura().getFechaYhora().toString();
    this.horaCierre = incidente.primerCierre().getFechaYhora().toString();
    this.establecimiento = new SRPEstablecimiento(incidente.getEstablecimiento());
    this.servicio = new SRPServicio(incidente.getServicio());
  }
}
