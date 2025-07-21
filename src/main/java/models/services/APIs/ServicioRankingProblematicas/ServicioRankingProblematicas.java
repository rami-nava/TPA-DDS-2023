package models.services.APIs.ServicioRankingProblematicas;

import models.domain.Entidades.Entidad;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;

import java.util.List;

public interface ServicioRankingProblematicas {
    List<Entidad> obtenerRanking(List<Entidad> entidades, List<Incidente> incidentes, List<Comunidad> comunidades, Long CNF);
}
