package controllers;

import io.javalin.http.Context;
import models.domain.Entidades.Entidad;
import models.domain.Entidades.EntidadPrestadora;
import models.domain.Entidades.OrganismoDeControl;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeEntidadPrestadora;
import models.persistence.Repositorios.RepositorioDeOrganismosDeControl;
import models.persistence.Repositorios.RepositorioEntidad;
import models.persistence.Repositorios.RepositorioMiembroDeComunidad;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadesController extends ControllerGenerico implements ICrudViewsHandler{
  RepositorioDeOrganismosDeControl repositorioDeOrganismosDeControl = RepositorioDeOrganismosDeControl.getInstancia();
  RepositorioDeEntidadPrestadora repositorioDeEntidadPrestadora = RepositorioDeEntidadPrestadora.getInstancia();
  RepositorioEntidad repositorioEntidad = RepositorioEntidad.getInstancia();

  @Override
  public void index(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;
    String organismo_id = context.pathParam("idO");
    String entidadPrestadora_id = context.pathParam("idEP");
    EntidadPrestadora entidadPrestadora = repositorioDeEntidadPrestadora.buscar(Long.parseLong(entidadPrestadora_id));
    List<Entidad> entidades = entidadPrestadora.getEntidades();
    List<Entidad> entidadesInteres = new ArrayList<>();
    List<Entidad> entidadesSinInteres = new ArrayList<>();

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
    {
      usuarioBasico = true;
      entidadesInteres = entidades.stream().filter(entidad ->  miembroDeComunidad.esEntidadDeInteres(entidad)).toList();
      entidadesSinInteres = entidades.stream().filter(entidad ->  !miembroDeComunidad.esEntidadDeInteres(entidad)).toList();
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
    model.put("entidades",entidades);
    model.put("entidadesInteres",entidadesInteres);
    model.put("entidadesSinInteres",entidadesSinInteres);
    model.put("miembro_id",miembroDeComunidad.getId());
    model.put("organismo_id",Long.parseLong(organismo_id));
    model.put("entidadPrestadora_id",Long.parseLong(entidadPrestadora_id));
    context.render("Entidades.hbs", model);
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
