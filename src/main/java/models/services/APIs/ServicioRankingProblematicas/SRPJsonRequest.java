package models.services.APIs.ServicioRankingProblematicas;

import lombok.Getter;
import lombok.Setter;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPComunidad;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPEntidad;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPIncidente;

import java.util.List;

@Setter
@Getter
public class SRPJsonRequest {
    private Long CNF;
    private List<SRPEntidad> entidades;
    private List<SRPIncidente> incidentes;
    private List<SRPComunidad> comunidades;
}
