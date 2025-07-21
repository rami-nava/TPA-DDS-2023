package controllers;

import io.javalin.http.Context;
import models.domain.Notificaciones.CuandoSuceden;
import models.domain.Notificaciones.ReceptorDeNotificaciones;
import models.domain.Notificaciones.ViaMail;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Seguridad.ValidadorDeContrasenias;
import models.domain.Usuario.Rol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeUsuarios;
import models.persistence.Repositorios.RepositorioMiembroDeComunidad;
import models.persistence.Repositorios.RepositorioRoles;
import server.exceptions.ContraseniaInvalida;
import server.exceptions.UsuarioRepetidoExcepcion;
import server.handlers.SessionHandler;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarController {

  public void index(Context context, String mensajeDeError) {
    Map<String, Object> model = new HashMap<>();

    model.put("hayError", true);
    model.put("mensajeDeError", mensajeDeError);
    context.render("LandingPage.hbs", model);
  }
  public void registrarUsuario(Context context){
    RepositorioDeUsuarios repositorioDeUsuarios = RepositorioDeUsuarios.getInstancia();
    RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
    RepositorioRoles repositorioRoles = RepositorioRoles.getInstancia();
    ValidadorDeContrasenias validadorDeContrasenias = new ValidadorDeContrasenias();
    EntityManager em = EntityManagerSingleton.getInstance();
    String nombre = context.formParam("nombre");
    String apellido = context.formParam("apellido");
    String telefono = context.formParam("telefono");
    String email = context.formParam("email");
    String usuario = context.formParam("usuario");
    String contrasenia = context.formParam("contrasenia");
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

        Rol basico = repositorioRoles.buscarTodos().stream().filter(rol -> rol.getNombre().equals("basico")).toList().get(0);
        nuevoUsuario.setRol(basico);

        repositorioDeUsuarios.agregar(nuevoUsuario);

        MiembroDeComunidad nuevoMiembro = new MiembroDeComunidad();
        nuevoMiembro.setNombre(nombre);
        nuevoMiembro.setApellido(apellido);
        nuevoMiembro.setUsuario(nuevoUsuario);

        ReceptorDeNotificaciones nuevoReceptor = new ReceptorDeNotificaciones();
        nuevoReceptor.setTelefono(telefono);
        nuevoReceptor.setMail(email);
        nuevoReceptor.cambiarFormaDeNotificar(new CuandoSuceden()); //default
        nuevoReceptor.cambiarMedioDeComunicacion(new ViaMail()); //default

        nuevoMiembro.setReceptorDeNotificaciones(nuevoReceptor);

        repositorioMiembroDeComunidad.agregar(nuevoMiembro);

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
}
