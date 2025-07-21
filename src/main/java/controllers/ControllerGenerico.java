package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.Usuario;
import models.persistence.Repositorios.RepositorioMiembroDeComunidad;
import server.exceptions.SesionNoIniciadaExcepcion;
import server.handlers.SessionHandler;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public abstract class ControllerGenerico implements WithSimplePersistenceUnit {
  protected Usuario usuarioLogueado(Context ctx, EntityManager em) {
    if(!SessionHandler.checkSession(ctx)) {
      throw new SesionNoIniciadaExcepcion();
    }
    SessionHandler.updateSession(ctx);
    return em
        .find(Usuario.class, Long.parseLong(SessionHandler.getUserID(ctx)));
  }

  protected MiembroDeComunidad miembroDelUsuario(String id){
    RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
    Optional<MiembroDeComunidad> posibleMiembro = Optional.of(new MiembroDeComunidad());
    List<MiembroDeComunidad> miembrosDeComunidadDelSistema;
    miembrosDeComunidadDelSistema = repositorioMiembroDeComunidad.buscarTodos();
    if(!miembrosDeComunidadDelSistema.isEmpty())
    {
      posibleMiembro = miembrosDeComunidadDelSistema.stream().
          filter(miembro -> miembro.getUsuario().getId().equals(Long.parseLong(id))).findFirst();
    }

    if(posibleMiembro.isPresent())
      return posibleMiembro.get();
    else
      return null;
  }


}
