package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.persistence.Persistente;

@Setter
@Getter
public abstract class SRPGenerica {
  protected Long id;
  protected SRPGenerica(Persistente persistente) {
    this.id = persistente.getId();
  }
}
