package controllers;

import io.javalin.http.Context;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioComunidad;
import models.persistence.Repositorios.RepositorioDeIncidentes;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComunidadesController extends ControllerGenerico implements ICrudViewsHandler {
  RepositorioComunidad repositorioComunidad = RepositorioComunidad.getInstancia();
  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context, em);
    boolean usuarioBasico = false;
    boolean administrador = false;
    List<Comunidad> usuarioComunidades;
    List<Comunidad> comunidades = new ArrayList<>();
    List<Comunidad> comunidadesUsuario = new ArrayList<>();

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());


    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      usuarioComunidades = miembroDeComunidad.getComunidades();
      comunidades = repositorioComunidad.buscarTodos().stream().filter(comunidad -> !usuarioComunidades.contains(comunidad)).toList();
      comunidadesUsuario = repositorioComunidad.buscarTodos().stream().filter(comunidad -> usuarioComunidades.contains(comunidad)).toList();
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
      comunidades = repositorioComunidad.buscarTodos();
    }

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",false);
    model.put("administrador",administrador);
    model.put("comunidades",comunidades);
    model.put("comunidadesUsuario",comunidadesUsuario);
    model.put("miembro_id",miembroDeComunidad.getId());
    context.render("UnirseComunidad.hbs", model);
    em.close();
  }

  @Override
  public void show(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    String id = context.pathParam("id");
    Comunidad comunidad = new Comunidad();
    comunidad = repositorioComunidad.buscar(Long.parseLong(id));

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }

    boolean miComunidad = false;

    if (miembroDeComunidad.getComunidades().contains(comunidad)) {
      miComunidad = true;
    }

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("comunidad",comunidad);
    model.put("miembro_id",miembroDeComunidad.getId());
    model.put("miComunidad",miComunidad);
    context.render("PerfilComunidad.hbs", model);
    em.close();
  }

  @Override
  public void create(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String nombre = context.pathParam("nombre");
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }

    List<String> nombresUtilizados = repositorioComunidad.buscarTodos().stream().map(comunidad -> comunidad.getNombre()).toList();
    if(nombresUtilizados.contains(nombre)){
      context.status(409);
    }
    else {
      try {
        em.getTransaction().begin();
        Comunidad nuevaComunidad = new Comunidad();
        nuevaComunidad.setNombre(nombre);
        nuevaComunidad.agregarMiembro(miembroDeComunidad); //el primer miembro es el administrador
        miembroDeComunidad.agregarComunidad(nuevaComunidad);
        repositorioComunidad.agregar(nuevaComunidad);
        em.getTransaction().commit();
      } catch (Exception e) {
        em.getTransaction().rollback();
      } finally {
        em.close();
      }
    }

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    context.render("UnirseComunidad.hbs", model);

  }

  @Override
  public void delete(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    String comunidadId = context.pathParam("id");
    try {
      em.getTransaction().begin();
      Comunidad comunidadAEliminar = repositorioComunidad.buscar(Long.parseLong(comunidadId));

      comunidadAEliminar.getMiembros().forEach(miembroDeComunidad -> miembroDeComunidad.abandonarComunidad(comunidadAEliminar));

      repositorioComunidad.eliminar(comunidadAEliminar);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void incidentesDeComunidad(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    String comunidadId = context.pathParam("id");
    Comunidad comunidad = repositorioComunidad.buscar(Long.parseLong(comunidadId));
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    List<Comunidad> comunidadesDelMiembro = miembroDeComunidad.getComunidades().stream().filter(comunidad1 -> !comunidad1.equals(comunidad)).toList();

    RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
    List<Incidente> incidentesDeComunidad = repositorioDeIncidentes.buscarTodos().
        stream().filter(incidente -> comunidad.incidenteEsDeComunidad(incidente) &&
            !comunidad.cerroIncidente(incidente)).toList();

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("miembro_id",miembroDeComunidad.getId());
    model.put("seleccion",true);
    model.put("comunidadSeleccionada",comunidad);
    model.put("comunidades",comunidadesDelMiembro);
    model.put("incidentes",incidentesDeComunidad);
    context.render("IncidentesPorComunidad.hbs", model);
    em.close();
  }

  public void incidentes(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    List<Comunidad> comunidadesDelMiembro = miembroDeComunidad.getComunidades();

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("miembro_id",miembroDeComunidad.getId());
    model.put("comunidades", comunidadesDelMiembro);
    model.put("seleccion",false);
    context.render("IncidentesPorComunidad.hbs", model);
    em.close();
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
}
