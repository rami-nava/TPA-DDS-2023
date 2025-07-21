package models.persistence.Repositorios;

import models.domain.Personas.ParServicioRol;


public class RepositorioParServicioRol extends RepositorioGenerico<ParServicioRol> {
  private static RepositorioParServicioRol instancia = null;
  private RepositorioParServicioRol() {
    super(ParServicioRol.class);
  }
  public static RepositorioParServicioRol getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioParServicioRol();
    }
    return instancia;
  }
}