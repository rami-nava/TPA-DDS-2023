package models.persistence.Repositorios;

import models.domain.Personas.Comunidad;

public class RepositorioComunidad extends RepositorioGenerico<Comunidad> {
  private static RepositorioComunidad instancia = null;

  private RepositorioComunidad() {
    super(Comunidad.class);
  }

  public static RepositorioComunidad getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioComunidad();
    }
    return instancia;
  }
}
