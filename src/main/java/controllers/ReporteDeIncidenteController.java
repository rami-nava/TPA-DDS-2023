package controllers;

import io.javalin.http.Context;
import models.domain.Incidentes.EstadoIncidente;
import models.domain.Incidentes.Incidente;
import models.domain.Incidentes.ReporteDeIncidente;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.*;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReporteDeIncidenteController extends ControllerGenerico implements ICrudViewsHandler {
  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    Map<String, Object> model = new HashMap<>();
    Usuario usuarioLogueado = super.usuarioLogueado(context,em);

    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());
    RepositorioEntidad repositorioEntidad = RepositorioEntidad.getInstancia();
    RepositorioDeEstablecimientos repositorioDeEstablecimientos = RepositorioDeEstablecimientos.getInstancia();


    model.put("usuarioBasico",true);
    model.put("comunidades", miembroDeComunidad.getComunidades());
    model.put("entidades", repositorioEntidad.buscarTodos());
    model.put("establecimientos", repositorioDeEstablecimientos.buscarTodos());
    model.put("miembro_id",miembroDeComunidad.getId());
    context.render("ReporteDeIncidente.hbs", model);
    em.close();
  }


  @Override
  public void save(Context context) {
    String entidad = context.formParam("entidad");
    String comunidad = context.formParam("comunidad");
    String observaciones = context.formParam("observaciones");
    String servicio = context.formParam("servicio");
    String establecimiento = context.formParam("establecimiento");
    String fechaYhora = context.formParam("fechaYhora");
    Comunidad comunidad1;
    EntityManager entityManager = EntityManagerSingleton.getInstance();
    Usuario usuarioLogueado = super.usuarioLogueado(context,entityManager);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    RepositorioEntidad repositorioEntidad=RepositorioEntidad.getInstancia();
    RepositorioServicio repositorioServicio=RepositorioServicio.getInstancia();
    RepositorioDeEstablecimientos repositorioDeEstablecimientos = RepositorioDeEstablecimientos.getInstancia();
    RepositorioComunidad repositorioComunidad=RepositorioComunidad.getInstancia();

    ReporteDeIncidente reporteDeIncidente = new ReporteDeIncidente();
    reporteDeIncidente.setClasificacion(EstadoIncidente.ABIERTO);
    reporteDeIncidente.setDenunciante(miembroDeComunidad);
    reporteDeIncidente.setEntidad(repositorioEntidad.buscar(Long.parseLong(entidad)));
    reporteDeIncidente.setObservaciones(observaciones);
    reporteDeIncidente.setServicio(repositorioServicio.buscar(Long.parseLong(servicio)));
    reporteDeIncidente.setFechaYhora(LocalDateTime.parse(fechaYhora));
    reporteDeIncidente.setEstablecimiento(repositorioDeEstablecimientos.buscar(Long.parseLong(establecimiento)));

    RepositorioDeReportesDeIncidentes repositorioDeReportesDeIncidentes = RepositorioDeReportesDeIncidentes.getInstancia();
    comunidad1 = repositorioComunidad.buscar(Long.parseLong(comunidad));

    try {
      entityManager.getTransaction().begin();
      this.guardarIncidenteComunidad(comunidad1, reporteDeIncidente, repositorioComunidad);
      repositorioDeReportesDeIncidentes.agregar(reporteDeIncidente);
      for (MiembroDeComunidad miembroDeComunidad1 : comunidad1.getMiembros()) {
        miembroDeComunidad1.recibirNotificacion(reporteDeIncidente);
      }
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      entityManager.getTransaction().rollback();
    } finally {
      entityManager.close();
    }
    context.redirect("/reporteDeIncidentes/ABIERTO");
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

  public void guardarIncidenteComunidad(Comunidad comunidad, ReporteDeIncidente reporteDeIncidente, RepositorioComunidad repositorioComunidad) {
    RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
    List<Incidente> incidentes = repositorioDeIncidentes.buscarTodos();
    List<Incidente> incidentesSobreLaMismaProblematica = incidentes.stream().filter(i -> i.getEstablecimiento().igualito(reporteDeIncidente.getEstablecimiento()) && i.getServicio().igualito(reporteDeIncidente.getServicio())).toList();

    if (incidentesSobreLaMismaProblematica.isEmpty()) //va a ser siempre de apertura
    {
      Incidente incidente = new Incidente();
      incidente.agregarReporteDeApertura(reporteDeIncidente);
      repositorioDeIncidentes.agregar(incidente);
    } else {
      boolean agregado = false;
      for (Incidente incidente : incidentesSobreLaMismaProblematica) {
        if (comunidad.incidenteEsDeComunidad(incidente) && !agregado && !comunidad.cerroIncidente(incidente)) {
          if(reporteDeIncidente.esDeCierre())
          {
            incidente.agregarReporteDeCierre(reporteDeIncidente);
            agregado = true;
          }
          else if(!reporteDeIncidente.esDeCierre())
          {
            incidente.agregarReporteDeApertura(reporteDeIncidente); //lo agrego, va a haber mas de un reporte de apertura de esta comunidad
            agregado = true;
          }
        }
        else if(!comunidad.incidenteEsDeComunidad(incidente) && !agregado) //primer incidente no abierto por la comunidad
        {
          incidente.agregarReporteDeApertura(reporteDeIncidente);
          agregado = true;
        }
      }
      if (!agregado) { //en principio siempre acá es de apertura
        Incidente incidente = new Incidente();
        incidente.agregarReporteDeApertura(reporteDeIncidente);
        repositorioDeIncidentes.agregar(incidente);
      }
    }
    comunidad.agregarReporte(reporteDeIncidente);
    repositorioComunidad.agregar(comunidad);
  }

  public void cerrarIncidente(Context context) {
    EntityManager em = EntityManagerSingleton.getInstance();
    RepositorioDeReportesDeIncidentes repositorioDeReportesDeIncidentes = RepositorioDeReportesDeIncidentes.getInstancia();
    RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
    String incidenteId = context.pathParam("idI");
    Incidente incidente =  repositorioDeIncidentes.buscar(Long.parseLong(incidenteId));

    RepositorioComunidad repositorioComunidad = RepositorioComunidad.getInstancia();
    String comunidadId = context.pathParam("idC");
    Comunidad comunidad = repositorioComunidad.buscar(Long.parseLong(comunidadId));


    Usuario usuarioLogueado = super.usuarioLogueado(context,em);
    MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

    if(!comunidad.cerroIncidente(incidente)) {
      ReporteDeIncidente reporteDeIncidente = new ReporteDeIncidente();
      reporteDeIncidente.setClasificacion(EstadoIncidente.valueOf("CERRADO"));
      reporteDeIncidente.setDenunciante(miembroDeComunidad);
      reporteDeIncidente.setEntidad(incidente.primeraApertura().getEntidad());
      reporteDeIncidente.setObservaciones("CERRADO");
      reporteDeIncidente.setServicio(incidente.getServicio());
      reporteDeIncidente.setFechaYhora(LocalDateTime.now());
      reporteDeIncidente.setEstablecimiento(incidente.getEstablecimiento());

      try {
        em.getTransaction().begin();
        this.guardarIncidenteComunidad(comunidad, reporteDeIncidente, repositorioComunidad);
        repositorioDeReportesDeIncidentes.agregar(reporteDeIncidente);
        em.getTransaction().commit();
      } catch (Exception e) {
        em.getTransaction().rollback();
      } finally {
        em.close();
      }
    }
    else {
      context.status(404);
    }
  }
}
