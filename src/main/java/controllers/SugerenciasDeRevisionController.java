package controllers;


import io.javalin.http.Context;
import models.Config.Config;
import models.domain.Incidentes.EstadoIncidente;
import models.domain.Incidentes.Incidente;
import models.domain.Entidades.Posicion;
import models.domain.Notificaciones.ReceptorDeNotificaciones;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.RepositorioDeIncidentes;
import models.persistence.Repositorios.RepositorioDeReceptoresDeNotificaciones;
import server.handlers.SessionHandler;
import server.utils.ICrudViewsHandler;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SugerenciasDeRevisionController extends ControllerGenerico implements ICrudViewsHandler {

    public void notificacion(Context context) {

        EntityManager em = EntityManagerSingleton.getInstance();
        Map<String, Object> model = new HashMap<>();
        Usuario usuarioLogueado = super.usuarioLogueado(context,em);
        String ubicacion = context.pathParam("ubicacion");
        String latitud = ubicacion.split(",")[0];
        String longitud = ubicacion.split(",")[1];
        if(!SessionHandler.checkLocationCookie(context)){

            SessionHandler.createLocationCookie(context);

            Posicion posicionUsuario = new Posicion();
            posicionUsuario.setPosicion(latitud+","+longitud);
            RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
            RepositorioDeReceptoresDeNotificaciones repositorioDeReceptoresDeNotificaciones = RepositorioDeReceptoresDeNotificaciones.getInstancia();
            boolean usuarioBasico = false;

            if(usuarioLogueado.getRol().getTipo() == TipoRol.USUARIO_BASICO)
            {
                usuarioBasico = true;
            }

            if (usuarioBasico){
                MiembroDeComunidad miembroDeComunidad = this.miembroDelUsuario(usuarioLogueado.getId().toString());

                List<Incidente> incidentesCercanos= new ArrayList<>();
                List<Incidente> incidentes= miembroDeComunidad.obtenerIncidentesPorEstado(EstadoIncidente.valueOf("ABIERTO"),
                        repositorioDeIncidentes.getIncidentesEstaSemana());

                incidentesCercanos = incidentes.stream().filter(i->i.getEstablecimiento().getPosicion()!=null&&
                        posicionUsuario.calcularDistancia(i.getEstablecimiento().getPosicion())<=Config.DISTANCIA_MINIMA).toList();

                boolean hayIncidentesCercanos = !incidentesCercanos.isEmpty();

                if(hayIncidentesCercanos) {
                    for (Incidente incidente: incidentesCercanos) {
                        ReceptorDeNotificaciones receptorDeNotificacionesUsuario=miembroDeComunidad.getReceptorDeNotificaciones();
                        receptorDeNotificacionesUsuario.recibirSolicitudDeRevision(incidente.primeraApertura());
                    }
                    context.result("POSITIVO").status(200);
                }
                else{
                    context.result("NEGATIVO").status(200);
                }
            }
            //context.status(200);
        }else {
            System.out.println("Respuesta del servidor: " + context.cookie("ubicacion"));
            context.status(200);
        }

        em.close();
    }

    public void solicitarIncidentes(Context context){
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

        model.put("miembro_id",miembroDeComunidad.getId());
        model.put("usuarioBasico",usuarioBasico);
        model.put("usuarioEmpresa",usuarioEmpresa);
        model.put("administrador",administrador);
        context.render("SolicitarIncidentes.hbs",model);
        em.close();
    }
    @Override
    public void index(Context context) {
        EntityManager em = EntityManagerSingleton.getInstance();
        Map<String, Object> model = new HashMap<>();
        Usuario usuarioLogueado = super.usuarioLogueado(context,em);
        String ubicacion = context.pathParam("ubicacion");
        String latitud = ubicacion.split(",")[0];
        String longitud = ubicacion.split(",")[1];
        Posicion posicionUsuario = new Posicion();
        posicionUsuario.setPosicion(latitud+","+longitud);
        RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
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

        List<Incidente> incidentesCercanos= new ArrayList<>();
        List<Incidente> incidentes= miembroDeComunidad.obtenerIncidentesPorEstado(EstadoIncidente.valueOf("ABIERTO"),
                                    repositorioDeIncidentes.getIncidentesEstaSemana());

        incidentesCercanos = incidentes.stream().filter(i->i.getEstablecimiento().getPosicion()!=null&&
                posicionUsuario.calcularDistancia(i.getEstablecimiento().getPosicion())<=Config.DISTANCIA_MINIMA).toList();

        boolean noHayIncidentes = incidentesCercanos.isEmpty();
        //Se puede pasarle la url para la referencia del js y el css poniendo la url en el config

        model.put("noHayIncidentes",noHayIncidentes);
        model.put("incidentesCercanos",incidentesCercanos);
        model.put("miembro_id",miembroDeComunidad.getId());
        model.put("usuarioBasico",usuarioBasico);
        model.put("usuarioEmpresa",usuarioEmpresa);
        model.put("administrador",administrador);
        context.render("SugerenciasDeRevisionDeIncidentes.hbs",model);
        context.status(200);
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
