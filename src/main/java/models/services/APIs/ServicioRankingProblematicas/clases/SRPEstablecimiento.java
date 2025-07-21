package models.services.APIs.ServicioRankingProblematicas.clases;


import lombok.Getter;
import lombok.Setter;
import models.domain.Entidades.Establecimiento;

@Setter
@Getter
public class SRPEstablecimiento extends SRPGenerica {
    public SRPEstablecimiento(Establecimiento establecimiento) {
        super(establecimiento);
    }
}
