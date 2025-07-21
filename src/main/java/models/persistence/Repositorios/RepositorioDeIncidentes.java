package models.persistence.Repositorios;


import models.domain.Incidentes.Incidente;

import java.util.List;

public class RepositorioDeIncidentes extends RepositorioGenerico<Incidente> {
  private static RepositorioDeIncidentes instancia = null;

  private RepositorioDeIncidentes() {
    super(Incidente.class);
  }
  public static  RepositorioDeIncidentes getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioDeIncidentes();
    }
    return instancia;
  }
  public List<Incidente> getIncidentesEstaSemana() { return this.buscarTodos().stream().filter(incidente -> incidente.primeraApertura().dentroDeEstaSemana()).toList(); }

}
