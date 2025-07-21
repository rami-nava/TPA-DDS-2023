package controllers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import models.domain.Entidades.Establecimiento;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Servicios.Servicio;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeEstablecimientos;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiciosController extends ControllerGenerico implements ICrudViewsHandler{
  RepositorioDeEstablecimientos repositorioDeEstablecimientos = RepositorioDeEstablecimientos.getInstancia();
  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    String organismoId = context.pathParam("idO");
    String entidadPrestadoraId = context.pathParam("idEP");
    String entidadId = context.pathParam("idE");
    String establecimientoId = context.pathParam("idES");
    String estado;

    Establecimiento establecimiento = repositorioDeEstablecimientos.buscar(Long.parseLong(establecimientoId));
    List<Servicio> servicios = establecimiento.getServicios();
    List<Servicio> serviciosInteresAfectado = new ArrayList<>();
    List<Servicio> serviciosInteresObservador = new ArrayList<>();
    List<Servicio> serviciosSinInteres = new ArrayList<>();

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      serviciosInteresObservador = servicios.stream().filter(servicio -> miembroDeComunidad.esObservador(servicio)).toList();
      serviciosInteresAfectado = servicios.stream().filter(servicio -> miembroDeComunidad.esAfectado(servicio)).toList();
      serviciosSinInteres = servicios.stream().filter(servicio -> !miembroDeComunidad.esServicioDeInteres(servicio)).toList();
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
    model.put("serviciosInteresObservador",serviciosInteresObservador);
    model.put("serviciosInteresAfectado",serviciosInteresAfectado);
    model.put("serviciosSinInteres",serviciosSinInteres);
    model.put("servicios",servicios);
    model.put("miembro_id",miembroDeComunidad.getId());
    model.put("organismo_id",organismoId);
    model.put("entidadPrestadora_id",entidadPrestadoraId);
    model.put("entidad_id",entidadId);
    model.put("establecimiento_id",establecimientoId);
    context.render("Servicios.hbs", model);
    em.close();

  }

  @Override
  public void show(Context context){

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

  public void obtenerServicios(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    String entidadId = context.pathParam("idE");
    String establecimientoId = context.pathParam("idES");
    Establecimiento establecimiento = repositorioDeEstablecimientos.buscar(Long.parseLong(establecimientoId));
    List<Servicio> servicios = establecimiento.getServicios();
    Gson gson = new Gson();
    String serviciosJson = gson.toJson(servicios);
    em.close();
    context.result(serviciosJson).contentType("application/json").status(200);
  }
}
