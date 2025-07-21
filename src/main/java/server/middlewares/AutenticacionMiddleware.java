package server.middlewares;

import io.javalin.config.JavalinConfig;
import models.domain.Usuario.TipoRol;
import server.exceptions.AccesoDenegadoExcepcion;
import server.exceptions.SesionNoIniciadaExcepcion;
import server.handlers.SessionHandler;

public class AutenticacionMiddleware {
  public static void apply(JavalinConfig config) {
    config.accessManager(((handler, context, routeRoles) -> {

      if(!context.path().equals("/") && !context.path().equals("/inicioDeSesion") &&!context.path().equals("/organismoDeControl/representante") && !context.path().equals("/entidadPrestadora/representante")) {
        if (SessionHandler.checkSession(context)) {
          TipoRol userRole = TipoRol.valueOf(SessionHandler.getTipoRol(context));
          if (routeRoles.size() == 0 || routeRoles.contains(userRole)) {
            handler.handle(context);
          } else {
            throw new AccesoDenegadoExcepcion();
          }
        } else {
          throw new SesionNoIniciadaExcepcion();
        }
      }
      else {
        if (SessionHandler.checkSession(context)) {
          context.redirect("/menu");
        }
        else{
          handler.handle(context);
        }
      }
    }));
  }
}
