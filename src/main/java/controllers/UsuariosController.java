package controllers;

import io.javalin.http.Context;
import models.domain.Incidentes.ReporteDeIncidente;
import models.domain.Notificaciones.*;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Seguridad.ValidadorDeContrasenias;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.*;
import server.exceptions.AccesoDenegadoExcepcion;
import server.exceptions.ContraseniaInvalida;
import server.exceptions.UsuarioRepetidoExcepcion;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuariosController extends ControllerGenerico implements ICrudViewsHandler {
  RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean administrador = false;

    List<MiembroDeComunidad> miembrosDeComunidadDelSistema = repositorioMiembroDeComunidad.buscarTodos();

    if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }
    model.put("administrador",administrador);
    model.put("miembros",miembrosDeComunidadDelSistema);
    model.put("miembro_id",this.miembroDelUsuario(usuarioLogueado.getId().toString()).getId());
    context.render("AdministracionUsuarios.hbs", model);
    em.close();
  }

  @Override
  public void show(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    boolean perfilAdministrador = false;
    String id = context.pathParam("id");
    RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();

    MiembroDeComunidad miembroDeComunidad = repositorioMiembroDeComunidad.buscar(Long.parseLong(id));
    Integer incidentesAbiertos = 0;
    Integer incidentesCerrados = 0;

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      incidentesAbiertos = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueAbiertoPor(miembroDeComunidad)).toList().size();
      incidentesCerrados = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueCerradoPor(miembroDeComunidad)).toList().size();
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
    }


    Usuario perfilUsuario = miembroDeComunidad.getUsuario();
    if(perfilUsuario.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      perfilAdministrador = false;
      incidentesAbiertos = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueAbiertoPor(miembroDeComunidad)).toList().size();
      incidentesCerrados = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueCerradoPor(miembroDeComunidad)).toList().size();
    }
    else if(perfilUsuario.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      perfilAdministrador = false;
    }
    else if(perfilUsuario.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      perfilAdministrador = true;
    }


    if (miembroDeComunidad.getId() != this.miembroDelUsuario(usuarioLogueado.getId().toString()).getId() && !administrador) { // Si no soy admin y no es mi perfil
      throw new AccesoDenegadoExcepcion();
    }


    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("perfilAdministrador",perfilAdministrador);
    model.put("miembroDeComunidad",miembroDeComunidad);
    model.put("incidentesAbiertos", incidentesAbiertos);
    model.put("incidentesCerrados", incidentesCerrados);
    model.put("miPerfil", false);
    context.render("PerfilUsuario.hbs", model);
    em.close();
  }

  @Override
  public void create(Context context) {

  }

  @Override
  public void save(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    RepositorioDeUsuarios repositorioDeUsuarios = RepositorioDeUsuarios.getInstancia();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = miembroDelUsuario(usuarioLogueado.getId().toString());
    ValidadorDeContrasenias validadorDeContrasenias = new ValidadorDeContrasenias();


    String usuario = context.formParam("usuario");
    String contrasenia = context.formParam("contrasenia");
    String nombre = context.formParam("nombre");
    String apellido = context.formParam("apellido");
    String mail = context.formParam("mail");
    String telefono = context.formParam("telefono");
    String medioDeComunicacion = context.formParam("medioDeComunicacion");
    String formaDeNotificar = context.formParam("formaDeNotificar");

    if(!usuarioLogueado.getUsername().equals(usuario))
    {
      List<String> usuariosRegistrados = repositorioDeUsuarios.buscarTodos().stream().map(usuario1 -> usuario1.getUsername()).toList();

      if(usuariosRegistrados.contains(usuario))
      {
        throw new UsuarioRepetidoExcepcion();
      }
    }

    if(!usuarioLogueado.getContrasenia().equals(contrasenia))
    {
      if(!validadorDeContrasenias.verificaReglas(contrasenia))
      {
        throw new ContraseniaInvalida();
      }
    }

    miembroDeComunidad.setNombre(nombre);
    miembroDeComunidad.setApellido(apellido);
    usuarioLogueado.setUsername(usuario);
    usuarioLogueado.setContrasenia(contrasenia);
    miembroDeComunidad.getReceptorDeNotificaciones().setMail(mail);
    miembroDeComunidad.getReceptorDeNotificaciones().setTelefono(telefono);
    try {
      em.getTransaction().begin();
      if (!(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)) {
        if (!miembroDeComunidad.getReceptorDeNotificaciones().getFormaDeNotificar().getClass().getSimpleName().equals(formaDeNotificar)) {
          switch (formaDeNotificar) {
            case "Cuando Suceden":
              miembroDeComunidad.getReceptorDeNotificaciones().cambiarFormaDeNotificar(new CuandoSuceden());
              break;
            case "Sin Apuros":
              miembroDeComunidad.getReceptorDeNotificaciones().cambiarFormaDeNotificar(new SinApuros());
              break;
          }
        }

        if (!miembroDeComunidad.getReceptorDeNotificaciones().getMedioDeComunicacion().getClass().getSimpleName().equals(medioDeComunicacion)) {
          switch (medioDeComunicacion) {
            case "Mail":
              miembroDeComunidad.getReceptorDeNotificaciones().cambiarMedioDeComunicacion(new ViaMail());
              break;
            case "Whatsapp":
              miembroDeComunidad.getReceptorDeNotificaciones().cambiarMedioDeComunicacion(new ViaWPP());
              break;
          }
        }
      }
      context.redirect("/perfil");
      em.getTransaction().commit();
    }catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  @Override
  public void edit(Context context) {
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

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("miembroDeComunidad",miembroDelUsuario(usuarioLogueado.getId().toString()));
    context.render("EditarPerfil.hbs", model);
    em.close();

  }
  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    String miembroId = context.pathParam("id");
    RepositorioDeReportesDeIncidentes repositorioDeReportesDeIncidentes = RepositorioDeReportesDeIncidentes.getInstancia();
    List<ReporteDeIncidente>  reportesDeIncidentes = repositorioDeReportesDeIncidentes.buscarTodos();
    try {
      em.getTransaction().begin();
      MiembroDeComunidad miembroDeComunidadAEliminar = repositorioMiembroDeComunidad.buscar(Long.parseLong(miembroId));
      List<ReporteDeIncidente> reportesAEliminar = reportesDeIncidentes.stream().filter(reporte -> reporte.getDenunciante().equals(miembroDeComunidadAEliminar)).toList();
      reportesAEliminar.forEach(reporte -> reporte.setDenunciante(null));
      repositorioMiembroDeComunidad.eliminar(miembroDeComunidadAEliminar);
      em.getTransaction().commit();
      if (usuarioLogueado.getId()==Long.parseLong(miembroId))
      { context.redirect("/inicioDeSesion"); }
      else context.redirect("/usuarios");
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

    public void abandonarComunidad(Context context) {
      EntityManager em = EntityManagerSingleton.getInstance();
      Usuario usuarioLogueado = super.usuarioLogueado(context,em);
      MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
      String id = context.pathParam("id");

      RepositorioComunidad repositorioComunidad = RepositorioComunidad.getInstancia();

      Comunidad comunidad = repositorioComunidad.buscar(Long.parseLong(id));

      miembroDeComunidad.abandonarComunidad(comunidad);

      try {
        em.getTransaction().begin();
        repositorioComunidad.agregar(comunidad);
        //update de miembro?
        em.getTransaction().commit();
      }
      catch (Exception e){
        em.getTransaction().rollback();
      }
      finally {
        em.close();
      }
    }

  public void unirseAComunidad(Context context) {
    RepositorioComunidad repositorioComunidad = RepositorioComunidad.getInstancia();
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    String comunidadId = context.pathParam("id");
    Comunidad comunidad = repositorioComunidad.buscar(Long.parseLong(comunidadId));

    comunidad.agregarMiembro(miembroDeComunidad);
    miembroDeComunidad.agregarComunidad(comunidad);

    try {
      em.getTransaction().begin();
      repositorioComunidad.agregar(comunidad);
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
    } finally {
      em.close();
    }
  }

  public void perfilPersonal(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    boolean perfilAdministrador = false;

    RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();

    MiembroDeComunidad miembroDeComunidad = miembroDelUsuario(usuarioLogueado.getId().toString());
    Integer incidentesAbiertos = 0;
    Integer incidentesCerrados = 0;

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      incidentesAbiertos = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueAbiertoPor(miembroDeComunidad)).toList().size();
      incidentesCerrados = repositorioDeIncidentes.buscarTodos().stream().filter(incidente -> incidente.fueCerradoPor(miembroDeComunidad)).toList().size();
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_EMPRESA)
    {
      usuarioEmpresa = true;
    }
    else if(usuarioLogueado.getRol().getTipo() == TipoRol.ADMINISTRADOR)
    {
      administrador = true;
      perfilAdministrador = true;
    }

    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("perfilAdministrador",perfilAdministrador);
    model.put("miembroDeComunidad",miembroDeComunidad);
    model.put("incidentesAbiertos", incidentesAbiertos);
    model.put("incidentesCerrados", incidentesCerrados);
    model.put("miPerfil", true);
    context.render("PerfilUsuario.hbs", model);
    em.close();
  }

  public void error(Context context, String mensajeDeError) {
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


    model.put("usuarioBasico",usuarioBasico);
    model.put("usuarioEmpresa",usuarioEmpresa);
    model.put("administrador",administrador);
    model.put("miembroDeComunidad",miembroDelUsuario(usuarioLogueado.getId().toString()));
    model.put("hayError", true);
    model.put("mensajeDeError", mensajeDeError);
    context.render("EditarPerfil.hbs", model);
    em.close();
  }
}
