package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Personas.MiembroDeComunidad;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class SRPMiembroDeComunidad extends SRPGenerica {
  private List<SRPServicio> serviciosQueAfectan;
  private List<SRPEstablecimiento> establecimientosDeInteres;

  public SRPMiembroDeComunidad(MiembroDeComunidad miembroDeComunidad) {
    super(miembroDeComunidad);
    this.serviciosQueAfectan = new ArrayList<>();
    this.establecimientosDeInteres = new ArrayList<>();
    miembroDeComunidad.getEntidadesDeInteres().forEach(e -> e.getEstablecimientos().forEach(est -> this.establecimientosDeInteres.add(new SRPEstablecimiento(est))));
    miembroDeComunidad.getServiciosDeInteres().forEach(ps -> this.serviciosQueAfectan.add(new SRPServicio(ps.getServicio())));
  }
}
