package models.services.APIs.ServicioRankingProblematicas.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Servicios.Servicio;

@Setter
@Getter
public class SRPServicio extends SRPGenerica {
    public SRPServicio(Servicio servicio) {
        super(servicio);
    }
}
