package models.domain.Usuario;

import io.javalin.security.RouteRole;

public enum TipoRol implements RouteRole {
  ADMINISTRADOR,
  USUARIO_BASICO,
  USUARIO_EMPRESA
}
