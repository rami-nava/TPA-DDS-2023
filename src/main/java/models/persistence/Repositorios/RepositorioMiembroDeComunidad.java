package models.persistence.Repositorios;

import models.domain.Personas.MiembroDeComunidad;


public class RepositorioMiembroDeComunidad extends RepositorioGenerico<MiembroDeComunidad> {
  private static RepositorioMiembroDeComunidad instancia = null;

  private RepositorioMiembroDeComunidad() {
    super(MiembroDeComunidad.class);
  }
  public static RepositorioMiembroDeComunidad getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioMiembroDeComunidad();
    }
    return instancia;
  }
}