package models.domain.Rankings;

import models.Config.Config;
import models.domain.Entidades.Entidad;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;
import models.services.APIs.ServicioRankingProblematicas.ServicioRankingProblematicas;
import models.services.APIs.ServicioRankingProblematicas.ServicioRankingProblematicasAPIREST;
import models.services.Archivos.SistemaDeArchivos;

import java.util.ArrayList;
import java.util.List;

public class EntidadesConMayorImpacto{
    private ServicioRankingProblematicas servicioRankingProblematicas = ServicioRankingProblematicasAPIREST.instancia();
    private Long CNF = 10L;

    public void armarRanking(List<Entidad> entidades, List<Incidente> incidentes, List< Comunidad > comunidades) {
        List<Entidad> entidadesOrdenadas = servicioRankingProblematicas.obtenerRanking(entidades, incidentes, comunidades, this.CNF);

        List<String[]> listaDeStrings = new ArrayList<>();
        entidadesOrdenadas.forEach(entidad -> listaDeStrings.add(new String[]{entidad.getNombre().toString(), entidad.getTipoEntidad().toString()}));

        SistemaDeArchivos sistemaDeArchivos = new SistemaDeArchivos();
        String[] encabezado = {"Nombre Entidad","Tipo Entidad"};
        sistemaDeArchivos.escribirRanking(Config.RANKING_3, encabezado, listaDeStrings);
    }
}
