package controllers;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import models.domain.Entidades.OrganismoDeControl;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeOrganismosDeControl;
import models.services.APIs.Georef.ServicioGeoref;
import models.services.Archivos.CargadorDeDatos;
import models.services.Archivos.SistemaDeArchivos;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpresasController extends ControllerGenerico implements ICrudViewsHandler {

  @Override
  public void index(Context context) {
  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
  }
@Override
  public void save(Context ctx) {
    UploadedFile uploadedFile = ctx.uploadedFile("archivoCSV");

    if (uploadedFile == null) {
      ctx.status(400).result("No se cargó el archivo");
      return;
    }

    Path outputPath = Path.of("resources", uploadedFile.filename());

    // Save the uploaded file to the specified path
    try (InputStream uploadedFileStream = uploadedFile.content()) {
      Files.copy(uploadedFileStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("Archivo guardado con éxito.");

      processFile(outputPath, ctx);

    } catch (IOException e) {
      ctx.status(500).result("Error al cargar el archivo");
      return;
    }
  }

  private void processFile(Path filePath, Context ctx) {
    EntityManager em = EntityManagerSingleton.getInstance();
    CargadorDeDatos cargadorDeDatos = new CargadorDeDatos();
    SistemaDeArchivos sistemaDeArchivos = new SistemaDeArchivos();
    ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
    RepositorioDeOrganismosDeControl repositorioDeOrganismosDeControl = RepositorioDeOrganismosDeControl.getInstancia();
    List<OrganismoDeControl> empresas;

    try {
      em.getTransaction().begin();
      empresas = cargadorDeDatos.cargaDeDatosMASIVA(sistemaDeArchivos.csvALista(filePath.toString()), servicioGeoref);
      empresas.forEach(e -> repositorioDeOrganismosDeControl.agregar(e));
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      e.printStackTrace();
      ctx.status(500).result("Error al cargar el archivo");
    } finally {
      em.close();
    }

    ctx.redirect("/empresas/cargar");
  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    boolean usuarioBasico = false;
    boolean usuarioEmpresa = false;
    boolean administrador = false;

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
    model.put("miembro_id",miembroDeComunidad.getId());
    context.render("CargaDeEntidadesYOrganismos.hbs", model);
    em.close();
  }

  @Override
  public void delete(Context context) {

  }
}
