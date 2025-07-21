package controllers;

import io.javalin.http.Context;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeUsuarios;
import server.exceptions.SesionNoIniciadaExcepcion;
import server.handlers.SessionHandler;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicioDeSesionController {

  public void validarCredenciales(Context context){
    RepositorioDeUsuarios repositorioDeUsuarios = RepositorioDeUsuarios.getInstancia();
    EntityManager em = EntityManagerSingleton.getInstance();
    String usuario = context.formParam("usuario");
    String contrasenia = context.formParam("contrasenia");
    List<Usuario> usuarioPosible = repositorioDeUsuarios.buscarTodos().stream().
                                  filter(u ->  u.getUsername().equals(usuario) && u.getContrasenia().equals(contrasenia)).toList();
    em.close();
    if(usuarioPosible.size()>0)
    {
      SessionHandler.createSession(context,usuarioPosible.get(0).getId(),usuarioPosible.get(0).getRol().getTipo().toString());
      context.redirect("/menu");
    }
    else
    {
      throw new SesionNoIniciadaExcepcion();
    }
  }

  public void verificarSesion(Context context){
    if(SessionHandler.checkSession(context))
      context.redirect("/menu");
    else{
      context.render("InicioDeSesion.hbs");
    }
  }

  public void error(Context context, String mensajeDeError) {
    Map<String, Object> model = new HashMap<>();

    model.put("hayError", true);
    model.put("mensajeDeError", mensajeDeError);
    context.render("InicioDeSesion.hbs", model);
  }
}

