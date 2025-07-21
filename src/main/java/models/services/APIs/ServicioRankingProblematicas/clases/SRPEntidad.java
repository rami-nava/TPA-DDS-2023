package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Entidades.Entidad;

import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
public class SRPEntidad extends SRPGenerica {
  private List<SRPEstablecimiento> establecimientos;

  public SRPEntidad(Entidad entidad) {
    super(entidad);
    this.establecimientos = new ArrayList<>();
    entidad.getEstablecimientos().forEach(e -> this.establecimientos.add(new SRPEstablecimiento(e)));
  }
}
