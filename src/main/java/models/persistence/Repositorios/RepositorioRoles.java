package models.persistence.Repositorios;

import models.domain.Usuario.Rol;

import java.util.ArrayList;
import java.util.List;

public class RepositorioRoles extends RepositorioGenerico<Rol>{
  private List<Rol> roles;
  private static RepositorioRoles instancia = null;

  private RepositorioRoles() {
    super(Rol.class);
    roles = new ArrayList<>();
  }

  public static  RepositorioRoles getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioRoles();
    }
    return instancia;
  }
}
