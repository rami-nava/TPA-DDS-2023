package server;

import controllers.*;
import models.domain.Usuario.TipoRol;
import server.handlers.SessionHandler;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {
  public static void init() {
    Server.app().routes(() -> {
      post("/cerrarSesion", ctx -> SessionHandler.endSession(ctx));
      get("/rankings", ((RankingsController) FactoryController.controller("rankings"))::index, TipoRol.USUARIO_EMPRESA,TipoRol.ADMINISTRADOR);
      get("/menu", ((MenuPrincipalController) FactoryController.controller("menuPrincipal"))::index);
      get("/", ctx -> ctx.render("LandingPage.hbs"));
      
      post("/", ((RegistrarController) FactoryController.controller("registrar"))::registrarUsuario);
      get("/inicioDeSesion", ((InicioDeSesionController) FactoryController.controller("inicioDeSesion"))::verificarSesion);
      post("/inicioDeSesion", ((InicioDeSesionController) FactoryController.controller("inicioDeSesion"))::validarCredenciales);
      get("/perfil/{id}",((UsuariosController) FactoryController.controller("usuarios"))::show, TipoRol.ADMINISTRADOR);
      get("/perfil",((UsuariosController) FactoryController.controller("usuarios"))::perfilPersonal);
      get("/editar/perfil", ((UsuariosController) FactoryController.controller("usuarios"))::edit);
      post("/editar/perfil", ((UsuariosController) FactoryController.controller("usuarios"))::save);
      get("/incidentes",((IncidentesController) FactoryController.controller("incidentes"))::indexQuery);
      get("/incidentes/{idI}/comunidad/{idC}/observaciones",((IncidentesController) FactoryController.controller("incidentes"))::observacionesComunidad);
      get("/incidentes/{idI}/observaciones",((IncidentesController) FactoryController.controller("incidentes"))::observacionesIncidente);
      get("/empresas/cargar", ((EmpresasController) FactoryController.controller("empresas"))::update,TipoRol.ADMINISTRADOR);
      post("/empresas/cargar",((EmpresasController) FactoryController.controller("empresas"))::save,TipoRol.ADMINISTRADOR);
      get("/reporteDeIncidentes/ABIERTO", ((ReporteDeIncidenteController) FactoryController.controller("reporteDeIncidente"))::create);
      post("/reporteDeIncidentes/ABIERTO", ((ReporteDeIncidenteController) FactoryController.controller("reporteDeIncidente"))::save);
      post("/reporteDeIncidentes/CERRADO/{idI}/{idC}", ((ReporteDeIncidenteController) FactoryController.controller("reporteDeIncidente"))::cerrarIncidente); /*Se usa??*/
      get("/sugerenciasDeRevision", ((SugerenciasDeRevisionController) FactoryController.controller("sugerenciaDeRevision"))::solicitarIncidentes);
      get("/comunidades", ((ComunidadesController) FactoryController.controller("comunidades"))::index,TipoRol.USUARIO_BASICO,TipoRol.ADMINISTRADOR);
      get("/comunidades/incidentes",((ComunidadesController) FactoryController.controller("comunidades"))::incidentes, TipoRol.USUARIO_BASICO, TipoRol.ADMINISTRADOR);
      get("/comunidades/incidentes/{id}",((ComunidadesController) FactoryController.controller("comunidades"))::incidentesDeComunidad, TipoRol.USUARIO_BASICO,TipoRol.ADMINISTRADOR);
      post("/comunidades/{id}/eliminar",((ComunidadesController) FactoryController.controller("comunidades"))::delete,TipoRol.ADMINISTRADOR);
      post("/comunidades/nueva/{nombre}",((ComunidadesController) FactoryController.controller("comunidades"))::create);
      post("/usuarios/unirseAComunidad/{id}",((UsuariosController) FactoryController.controller("usuarios"))::unirseAComunidad);
      post("/usuarios/abandonarComunidad/{id}",((UsuariosController) FactoryController.controller("usuarios"))::abandonarComunidad);
      get("/comunidades/{id}",((ComunidadesController) FactoryController.controller("comunidades"))::show);
      get("/usuarios",((UsuariosController) FactoryController.controller("usuarios"))::index, TipoRol.ADMINISTRADOR);
      post("/usuarios/{id}/eliminar",((UsuariosController) FactoryController.controller("usuarios"))::delete, TipoRol.ADMINISTRADOR);
      post("/usuarios/{id}/interes/entidad",((UsuariosController) FactoryController.controller("usuarios"))::update,TipoRol.USUARIO_BASICO); //chequear
      get("/organismosDeControl",((OrganismosDeControlController) FactoryController.controller("organismos"))::index);
      get("/organismosDeControl/{id}/entidadesPrestadoras",((EntidadesPrestadorasController) FactoryController.controller("entidadesPrestadoras"))::index);
      get("/organismosDeControl/{idO}/entidadesPrestadoras/{idEP}/entidades", ((EntidadesController) FactoryController.controller("entidades"))::index);
      get("/organismosDeControl/{idO}/entidadesPrestadoras/{idEP}/entidades/{idE}/establecimientos",((EstablecimientosController) FactoryController.controller("establecimientos"))::index);
      get("/organismosDeControl/{idO}/entidadesPrestadoras/{idEP}/entidades/{idE}/establecimientos/{idES}/servicios",((ServiciosController) FactoryController.controller("servicios"))::index);
      get("/interesEntidad/{id}",((InteresController) FactoryController.controller("interes"))::verificarInteresEntidad,TipoRol.USUARIO_BASICO);
      get("/entidades/{idE}/establecimientos",((EstablecimientosController) FactoryController.controller("establecimientos"))::obtenerEstablecimientos,TipoRol.USUARIO_BASICO);
      get("/entidades/{idE}/establecimientos/{idES}/servicios",((ServiciosController) FactoryController.controller("servicios"))::obtenerServicios,TipoRol.USUARIO_BASICO);
      get("/intereses/entidades",((InteresController) FactoryController.controller("interes"))::indexEntidades,TipoRol.USUARIO_BASICO);
      get("/intereses/servicios",((InteresController) FactoryController.controller("interes"))::indexServicios,TipoRol.USUARIO_BASICO);
      get("/intereses/entidad/{id}",((InteresController) FactoryController.controller("interes"))::verificarInteresEntidad,TipoRol.USUARIO_BASICO);
      get("/intereses/servicio/{id}",((InteresController) FactoryController.controller("interes"))::verificarInteresServicio,TipoRol.USUARIO_BASICO);
      post("/intereses/entidad/{id}/agregar",((InteresController) FactoryController.controller("interes"))::agregarEntidad,TipoRol.USUARIO_BASICO);
      post("/intereses/entidad/{id}/eliminar",((InteresController) FactoryController.controller("interes"))::eliminarEntidad,TipoRol.USUARIO_BASICO);
      post("/intereses/servicio/{id}/agregar/{rol}",((InteresController) FactoryController.controller("interes"))::agregarParServicioRolInteres,TipoRol.USUARIO_BASICO);
      post("/intereses/servicio/{id}/eliminar",((InteresController) FactoryController.controller("interes"))::eliminarParServicioRolInteres,TipoRol.USUARIO_BASICO);
      post("/intereses/servicio/{id}/cambiarRol",((InteresController) FactoryController.controller("interes"))::cambiarRolDelParServicioRol,TipoRol.USUARIO_BASICO);
      post("/servicio/{id}/cambiarRol",((InteresController) FactoryController.controller("interes"))::cambiarRolDelServicio,TipoRol.USUARIO_BASICO);
      post("/servicio/{id}/eliminar",((InteresController) FactoryController.controller("interes"))::eliminarServicioInteres,TipoRol.USUARIO_BASICO);
      get("/sugerenciasDeRevision/{ubicacion}",((SugerenciasDeRevisionController) FactoryController.controller("sugerenciaDeRevision"))::index,TipoRol.USUARIO_BASICO);
      get("/organismoDeControl/representante",((OrganismosDeControlController) FactoryController.controller("organismos"))::registrarUsuario);
      get("/entidadPrestadora/representante",((EntidadesPrestadorasController) FactoryController.controller("entidadesPrestadoras"))::registrarUsuario);
      post("/organismoDeControl/representante",((OrganismosDeControlController) FactoryController.controller("organismos"))::usuarioOrganismoDeControl);
      post("/entidadPrestadora/representante",((EntidadesPrestadorasController) FactoryController.controller("entidadesPrestadoras"))::usuarioEntidadPrestadora);
      get("/sugerenciasDeRevision/{ubicacion}/notificacion",((SugerenciasDeRevisionController) FactoryController.controller("sugerenciaDeRevision"))::notificacion,TipoRol.USUARIO_BASICO);
    });
  }
}
