package controllers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import models.domain.Entidades.Entidad;
import models.domain.Entidades.Establecimiento;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.*;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstablecimientosController extends ControllerGenerico implements ICrudViewsHandler{
  RepositorioEntidad repositorioEntidad = RepositorioEntidad.getInstancia();
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

      Entidad entidad = repositorioEntidad.buscar(Long.parseLong(entidadId));
      List<Establecimiento> establecimientos = entidad.getEstablecimientos();
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
      model.put("establecimientos",establecimientos);
      model.put("miembro_id",miembroDeComunidad.getId());
      model.put("organismo_id",organismoId);
      model.put("entidadPrestadora_id",entidadPrestadoraId);
      model.put("entidad_id",entidadId);
      context.render("Establecimientos.hbs", model);
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

  public void obtenerEstablecimientos(Context context){
    EntityManager em = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    String entidadId = context.pathParam("idE");

    Entidad entidad = repositorioEntidad.buscar(Long.parseLong(entidadId));
    List<Establecimiento> establecimientos = entidad.getEstablecimientos();
    Gson gson = new Gson();
    String establecimientosJson = gson.toJson(establecimientos);
    em.close();
    context.result(establecimientosJson).contentType("application/json").status(200);
  }
}
