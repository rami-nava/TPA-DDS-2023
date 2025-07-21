package models.persistence.Repositorios;


import models.domain.Incidentes.ReporteDeIncidente;
import lombok.Getter;


@Getter
public class RepositorioDeReportesDeIncidentes extends RepositorioGenerico<ReporteDeIncidente> {

    private static RepositorioDeReportesDeIncidentes instancia = null;

    private RepositorioDeReportesDeIncidentes() {
        super(ReporteDeIncidente.class);
    }
    public static  RepositorioDeReportesDeIncidentes getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeReportesDeIncidentes();
        }
        return instancia;
    }
}