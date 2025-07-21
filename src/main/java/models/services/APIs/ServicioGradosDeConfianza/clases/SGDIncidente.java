package models.services.APIs.ServicioGradosDeConfianza.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Incidentes.Incidente;

import java.time.format.DateTimeFormatter;


@Setter
@Getter
public class SGDIncidente {
  private Long id;
  private String fechaApertura;
  private SGDUsuario usuarioReportador;
  private String fechaCierre;
  private SGDUsuario usuarioAnalizador;
  private SGDServicio servicioAfectado;

  public SGDIncidente(Incidente incidente) {
    this.setId(incidente.getId());

    SGDServicio servicio = new SGDServicio();
    servicio.setId(incidente.getServicio().getId());
    this.setServicioAfectado(servicio);

    this.setUsuarioReportador(new SGDUsuario(incidente.primeraApertura().getDenunciante()));

    this.setFechaApertura(incidente.primeraApertura().getFechaYhora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

    this.setFechaCierre(incidente.primerCierre().getFechaYhora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

    this.setUsuarioAnalizador(new SGDUsuario(incidente.primerCierre().getDenunciante()));
  }
}
