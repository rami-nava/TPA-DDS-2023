package controllers;

import io.javalin.http.Context;
import models.domain.Entidades.Entidad;
import models.domain.Entidades.Establecimiento;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Personas.ParServicioRol;
import models.domain.Personas.Rol;
import models.domain.Servicios.Servicio;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeEstablecimientos;
import models.persistence.Repositorios.RepositorioEntidad;
import models.persistence.Repositorios.RepositorioParServicioRol;
import models.persistence.Repositorios.RepositorioServicio;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteresController extends ControllerGenerico{
  RepositorioEntidad repositorioEntidad = RepositorioEntidad.getInstancia();
  RepositorioServicio repositorioServicio = RepositorioServicio.getInstancia();
  RepositorioParServicioRol repositorioParServicioRol = RepositorioParServicioRol.getInstancia();

  public void verificarInteresEntidad(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    Entidad entidad = repositorioEntidad.buscar(Long.parseLong(context.pathParam("id")));
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    boolean interes = miembroDeComunidad.esEntidadDeInteres(entidad);
    em.close();
    context.json(interes);
  }

  public void verificarInteresServicio(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    Servicio servicio = repositorioServicio.buscar(Long.parseLong(context.pathParam("id")));
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    boolean interes = miembroDeComunidad.esServicioDeInteres(servicio);
    em.close();
    context.json(interes);
  }

  public void indexEntidades(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    List<Entidad> entidades = miembroDeComunidad.getEntidadesDeInteres();

    model.put("usuarioBasico",true);
    model.put("entidades",entidades);
    model.put("miembro_id",miembroDeComunidad.getId());
    context.render("InteresesEntidades.hbs", model);
    em.close();
  }

  public void indexServicios(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    RepositorioDeEstablecimientos repositorioDeEstablecimientos = RepositorioDeEstablecimientos.getInstancia();

    List<ParServicioRol> servicios = miembroDeComunidad.getServiciosDeInteres();

    Long establecimientoDeServicioId;
    Establecimiento establecimientoDeServicio;

    for(ParServicioRol parServicioRol:servicios)
    {
      establecimientoDeServicioId = repositorioServicio.establecimientoDeServicio(parServicioRol.getServicio().getId());

      establecimientoDeServicio = repositorioDeEstablecimientos.buscar(establecimientoDeServicioId);
      parServicioRol.getServicio().setEstablecimiento(establecimientoDeServicio);

    }

    model.put("usuarioBasico",true);
    model.put("servicios",servicios);
    context.render("InteresesServicios.hbs", model);
    em.close();
  }

  public void agregarEntidad(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String entidadId = context.pathParam("id");
    try {
      em.getTransaction().begin();
      Entidad nuevaEntidadInteres = repositorioEntidad.buscar(Long.parseLong(entidadId));
      miembroDeComunidad.agregarEntidadDeInteres(nuevaEntidadInteres);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }
  public void eliminarEntidad(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String entidadId = context.pathParam("id");
    try {
      em.getTransaction().begin();
      Entidad entidadAEliminar = repositorioEntidad.buscar(Long.parseLong(entidadId));
      miembroDeComunidad.eliminarEntidadDeInteres(entidadAEliminar);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void agregarParServicioRolInteres(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String servicioId = context.pathParam("id");
    String rol = context.pathParam("rol");
    try {
      em.getTransaction().begin();
      Servicio nuevoServicioDeInteres = repositorioServicio.buscar(Long.parseLong(servicioId));
      miembroDeComunidad.agregarServicioDeInteres(nuevoServicioDeInteres, Rol.valueOf(rol));
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }
  public void eliminarParServicioRolInteres(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String servicioId = context.pathParam("id");
    try {
      em.getTransaction().begin();
      ParServicioRol parServicioRolAEliminar = repositorioParServicioRol.buscar(Long.parseLong(servicioId));
      miembroDeComunidad.eliminarServicioDeInteres(parServicioRolAEliminar);
      //En una relacion OneToMany no elimina la fila hibernate con tan solo quitarlo de la lista del miebro de comunidad
      repositorioParServicioRol.eliminar(parServicioRolAEliminar);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void cambiarRolDelParServicioRol(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    String servicioId = context.pathParam("id");

    try {
      em.getTransaction().begin();
      ParServicioRol parServicioRolAModificar = repositorioParServicioRol.buscar(Long.parseLong(servicioId));
      parServicioRolAModificar.cambiarRol();
      //En una relacion OneToMany no elimina la fila hibernate con tan solo quitarlo de la lista del miebro de comunidad
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void cambiarRolDelServicio(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    String servicioId = context.pathParam("id");
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = miembroDelUsuario(usuarioLogueado.getId().toString());

    Servicio servicioSeleccionado = repositorioServicio.buscar(Long.parseLong(servicioId));
    List<ParServicioRol> pares = miembroDeComunidad.getServiciosDeInteres();

    try {
      em.getTransaction().begin();
      ParServicioRol parServicioRolAModificar = pares.stream().filter(parServicioRol -> parServicioRol.getServicio().equals(servicioSeleccionado)).toList().get(0);
      parServicioRolAModificar.cambiarRol();
      //En una relacion OneToMany no elimina la fila hibernate con tan solo quitarlo de la lista del miebro de comunidad
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void eliminarServicioInteres(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String servicioId = context.pathParam("id");

    Servicio servicioSeleccionado = repositorioServicio.buscar(Long.parseLong(servicioId));
    List<ParServicioRol> pares = miembroDeComunidad.getServiciosDeInteres();

    try {
      em.getTransaction().begin();
      ParServicioRol parServicioRolAEliminar = pares.stream().filter(parServicioRol -> parServicioRol.getServicio().equals(servicioSeleccionado)).toList().get(0);
      miembroDeComunidad.eliminarServicioDeInteres(parServicioRolAEliminar);
      //En una relacion OneToMany no elimina la fila hibernate con tan solo quitarlo de la lista del miebro de comunidad
      repositorioParServicioRol.eliminar(parServicioRolAEliminar);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }
}
