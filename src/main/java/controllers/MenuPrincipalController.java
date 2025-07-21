package controllers;

import io.javalin.http.Context;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuPrincipalController extends ControllerGenerico implements ICrudViewsHandler  {

  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context, em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    List<Comunidad> usuarioComunidades = new ArrayList<>();
    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      usuarioComunidades = miembroDeComunidad.getComunidades();
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }
    /*model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("miembro_id",this.miembroDelUsuario(usuarioLogueado.getId().toString()).getId());*/
    if(administrador || usuarioEmpresa){
      context.redirect("/incidentes");
    }
    if (usuarioBasico){
      System.out.println(usuarioComunidades);
      System.out.println(usuarioComunidades.isEmpty());
      if (!usuarioComunidades.isEmpty()) {
        context.redirect("/incidentes?estado=ABIERTO");
      }else context.redirect("/comunidades");
    }
    //context.render("MenuPrincipal.hbs", model);
    em.close();
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {

  }

  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}
