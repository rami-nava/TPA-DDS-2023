package server.handlers;

import controllers.FactoryController;
import controllers.InicioDeSesionController;
import io.javalin.Javalin;
import server.exceptions.SesionNoIniciadaExcepcion;

public class SesionNoIniciadaHandler implements IHandler {
  @Override
  public void setHandle(Javalin app) {
    app.exception(SesionNoIniciadaExcepcion.class, (e, context) -> {
      if(context.path().equals("/inicioDeSesion")) {
        ((InicioDeSesionController) FactoryController.controller("inicioDeSesion")).error(context, "Usuario o contraseña inválido");
      }else {
        context.redirect("/");
      }
    });
  }
}
