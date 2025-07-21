package controllers;

import io.javalin.http.Context;
import models.domain.Entidades.OrganismoDeControl;
import models.domain.Notificaciones.CuandoSuceden;
import models.domain.Notificaciones.ReceptorDeNotificaciones;
import models.domain.Notificaciones.ViaMail;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Seguridad.ValidadorDeContrasenias;
import models.domain.Usuario.Rol;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeOrganismosDeControl;
import models.persistence.Repositorios.RepositorioDeUsuarios;
import models.persistence.Repositorios.RepositorioMiembroDeComunidad;
import models.persistence.Repositorios.RepositorioRoles;
import server.exceptions.ContraseniaInvalida;
import server.exceptions.UsuarioRepetidoExcepcion;
import server.handlers.SessionHandler;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganismosDeControlController extends ControllerGenerico implements ICrudViewsHandler{
  RepositorioDeOrganismosDeControl repositorioDeOrganismosDeControl = RepositorioDeOrganismosDeControl.getInstancia();
  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    List<OrganismoDeControl> organismos = repositorioDeOrganismosDeControl.buscarTodos();
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

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
    model.put("organismos",organismos);
    model.put("miembro_id",miembroDeComunidad.getId());
    context.render("OrganismosDeControl.hbs", model);
    em.close();

  }

  public void registrarUsuario(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    List<OrganismoDeControl> organismos = repositorioDeOrganismosDeControl.buscarTodos();
    model.put("empresas",organismos);
    model.put("organismoDeControl",true);
    context.render("UsuarioEmpresa.hbs", model);
    em.close();
  }

  public void usuarioOrganismoDeControl(Context context) {
    RepositorioDeUsuarios repositorioDeUsuarios = RepositorioDeUsuarios.getInstancia();
    RepositorioRoles repositorioRoles = RepositorioRoles.getInstancia();
    RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
    ValidadorDeContrasenias validadorDeContrasenias = new ValidadorDeContrasenias();
    EntityManager em = EntityManagerSingleton.getInstance();

    String email = context.formParam("email");
    String usuario = context.formParam("usuario");
    String contrasenia = context.formParam("contrasenia");
    String organismoDeControlId = context.formParam("empresaId");
    OrganismoDeControl organismoDeControl = repositorioDeOrganismosDeControl.buscar(Long.parseLong(organismoDeControlId));

    List<String> usuariosRegistrados = repositorioDeUsuarios.buscarTodos().stream().map(usuario1 -> usuario1.getUsername()).toList();

    if(usuariosRegistrados.contains(usuario))
    {
      throw new UsuarioRepetidoExcepcion();
    }
    else if(!validadorDeContrasenias.verificaReglas(contrasenia))
    {
      throw new ContraseniaInvalida();
    }
    else{
      try {
        em.getTransaction().begin();

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(usuario);
        nuevoUsuario.setContrasenia(contrasenia);

        Rol empresa = repositorioRoles.buscarTodos().stream().filter(rol -> rol.getNombre().equals("empresa")).toList().get(0);
        nuevoUsuario.setRol(empresa);

        repositorioDeUsuarios.agregar(nuevoUsuario);

        organismoDeControl.setPersonaMail(email);

        MiembroDeComunidad miembroDeComunidad = new MiembroDeComunidad();
        miembroDeComunidad.setUsuario(nuevoUsuario);
        miembroDeComunidad.setNombre(organismoDeControl.getNombre());

        ReceptorDeNotificaciones nuevoReceptor = new ReceptorDeNotificaciones();
        nuevoReceptor.setMail(email);
        nuevoReceptor.cambiarFormaDeNotificar(new CuandoSuceden());
        nuevoReceptor.cambiarMedioDeComunicacion(new ViaMail());

        miembroDeComunidad.setReceptorDeNotificaciones(nuevoReceptor);
        repositorioMiembroDeComunidad.agregar(miembroDeComunidad);

        SessionHandler.createSession(context,nuevoUsuario.getId(),nuevoUsuario.getRol().getTipo().toString());
        context.redirect("/menu");
        em.getTransaction().commit();
      } catch (Exception e) {
        em.getTransaction().rollback();
      } finally {
        em.close();
      }
    }
  }

  public void error(Context context, String mensajeDeError) {
    Map<String, Object> model = new HashMap<>();

    List<OrganismoDeControl> organismos = repositorioDeOrganismosDeControl.buscarTodos();
    model.put("empresas",organismos);
    model.put("organismoDeControl",true);
    model.put("hayError", true);
    model.put("mensajeDeError", mensajeDeError);
    context.render("UsuarioEmpresa.hbs", model);
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
