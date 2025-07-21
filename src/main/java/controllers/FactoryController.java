package controllers;

public class FactoryController {

  public static Object controller(String nombre) {
    Object controller = null;
    switch (nombre) {
      case "rankings": controller = new RankingsController(); break;
      case "menuPrincipal": controller = new MenuPrincipalController(); break;
      case "usuarios": controller = new UsuariosController();break;
      case "reporteDeIncidente": controller = new ReporteDeIncidenteController();break;
      case "incidentes": controller = new IncidentesController();break;
      case "empresas": controller = new EmpresasController();break;
      case "comunidades": controller = new ComunidadesController();break;
      case "inicioDeSesion": controller = new InicioDeSesionController();break;
      case "organismos": controller = new OrganismosDeControlController();break;
      case "entidadesPrestadoras": controller = new EntidadesPrestadorasController();break;
      case "entidades": controller = new EntidadesController();break;
      case "establecimientos": controller = new EstablecimientosController();break;
      case "servicios": controller = new ServiciosController();break;
      case "interes": controller = new InteresController();break;
      case "sugerenciaDeRevision":controller = new SugerenciasDeRevisionController();break;
      case "registrar":controller = new RegistrarController();break;
    }
    return controller;
  }
}
