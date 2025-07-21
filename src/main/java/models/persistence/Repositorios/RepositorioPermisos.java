package models.persistence.Repositorios;

import models.domain.Usuario.Permiso;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPermisos extends RepositorioGenerico<Permiso>{
  private List<Permiso> permisos;
  private static RepositorioPermisos instancia = null;

  private RepositorioPermisos() {
    super(Permiso.class);
    permisos = new ArrayList<>();
  }

  public static  RepositorioPermisos getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioPermisos();
    }
    return instancia;
  }
}
