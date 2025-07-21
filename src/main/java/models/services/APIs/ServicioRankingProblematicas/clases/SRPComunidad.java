package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class SRPComunidad extends SRPGenerica {
  private List<SRPIncidente> incidentes;
  private List<SRPMiembroDeComunidad> miembros;

  public SRPComunidad(Comunidad comunidad, List<Incidente> incidentes) {
    super(comunidad);
    this.incidentes = new ArrayList<>();
    this.miembros = new ArrayList<>();
    comunidad.getMiembros().forEach(m -> this.miembros.add(new SRPMiembroDeComunidad(m)));
    comunidad.getIncidentesDeComunidad(incidentes).forEach(i -> this.incidentes.add(new SRPIncidente(i)));
  }
}
