package Personas;

import models.domain.Entidades.Entidad;
import models.domain.Entidades.Establecimiento;
import models.domain.Entidades.TipoEntidad;
import models.domain.Entidades.TipoEstablecimiento;
import models.domain.Notificaciones.CuandoSuceden;
import models.domain.Notificaciones.FormaDeNotificar;
import models.domain.Notificaciones.MedioDeComunicacion;
import models.domain.Notificaciones.ViaMail;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Personas.ParServicioRol;
import models.domain.Personas.Rol;
import models.domain.Servicios.Banio;
import models.domain.Servicios.Servicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import models.services.APIs.Georef.AdapterServicioGeo;
import models.services.Localizacion.Municipio;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestInteresesMiembro {
  @Mock
  private AdapterServicioGeo servicioGeo;
  private MiembroDeComunidad miembro;
  private Municipio generalAlvarado;
  private Municipio pinamar;
  private Servicio banioHombres;
  private Servicio banioMujeres;
  private Establecimiento estacionRetiro;
  private Establecimiento estacionTolosa;
  private Establecimiento estacionPinamar;
  private Entidad lineaMitre;
  private Entidad lineaRoca;
  private FormaDeNotificar cuandoSuceden = new CuandoSuceden();
  private MedioDeComunicacion mail = new ViaMail();
  
  @BeforeEach
  public void init(){
    miembro = new MiembroDeComunidad();
    miembro.setNombre("jose");
    miembro.setApellido("perez");
    miembro.getReceptorDeNotificaciones().cambiarFormaDeNotificar(cuandoSuceden);
    miembro.getReceptorDeNotificaciones().cambiarMedioDeComunicacion(mail);
    miembro.getReceptorDeNotificaciones().setMail("perezjose@gmail.com");
    miembro.getReceptorDeNotificaciones().setTelefono("123456789");

    MockitoAnnotations.openMocks(this);
    servicioGeo = mock(AdapterServicioGeo.class);
    when(servicioGeo.obtenerMunicipio("General Alvarado")).thenReturn(generalAlvarado);
    when(servicioGeo.obtenerMunicipio("Pinamar")).thenReturn(pinamar);

    banioHombres = new Banio();
    banioHombres.setTipo("CABALLEROS");
    banioMujeres = new Banio();
    banioMujeres.setTipo("DAMAS");

    miembro.agregarServicioDeInteres(banioHombres, Rol.valueOf("AFECTADO"));

    lineaMitre = new Entidad();
    lineaMitre.setNombre("Linea Mitre");
    lineaMitre.setTipoEntidad(TipoEntidad.FERROCARRIL);

    estacionRetiro = new Establecimiento();
    estacionRetiro.setNombre("Retiro");
    estacionRetiro.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionRetiro.setLocalizacion(generalAlvarado);
    estacionRetiro.agregarServicio(banioHombres);

    estacionPinamar = new Establecimiento();
    estacionPinamar.setNombre("Pinamar");
    estacionPinamar.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionPinamar.setLocalizacion(pinamar);
    estacionPinamar.agregarServicio(banioHombres);

    lineaMitre.agregarEstablecimiento(estacionRetiro);
    lineaMitre.agregarEstablecimiento(estacionPinamar);

    lineaRoca = new Entidad();
    lineaRoca.setNombre("Linea Roca");
    lineaRoca.setTipoEntidad(TipoEntidad.FERROCARRIL);
    lineaRoca.agregarEstablecimiento(estacionTolosa);

    estacionTolosa = new Establecimiento();
    estacionTolosa.setNombre("Tolosa");
    estacionTolosa.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionTolosa.setLocalizacion(generalAlvarado);
    estacionTolosa.agregarServicio(banioMujeres);
  }

  @Test
  public void leInteresaElServicioYElEstablecimiento(){
    miembro.agregarEntidadDeInteres(lineaMitre);
    Mockito.when(servicioGeo.obtenerMunicipio("General Alvarado")).thenReturn(generalAlvarado);
    Assertions.assertTrue(miembro.tieneInteres(banioHombres, estacionRetiro));
  }

  @Test
  public void noLeInteresaElServicio(){
    miembro.agregarEntidadDeInteres(lineaRoca);
    Mockito.when(servicioGeo.obtenerMunicipio("General Alvarado")).thenReturn(generalAlvarado);
    Assertions.assertFalse(miembro.tieneInteres(banioMujeres, estacionTolosa));
  }

  @Test
  public void leInteresaElServicioPeroNoElEstablecimiento(){
    Mockito.when(servicioGeo.obtenerMunicipio("General Alvarado")).thenReturn(generalAlvarado);
    Assertions.assertFalse(miembro.tieneInteres(banioHombres, estacionRetiro));
  }

  @Test
  public void leInteresaPeroNoQuedaCerca(){
    miembro.agregarEntidadDeInteres(lineaMitre);
    Assertions.assertTrue(miembro.tieneInteres(banioHombres,estacionPinamar));
  }

  @Test
  public void CambiarRolDeServicioDeInters(){
    ParServicioRol banioYrol = miembro.getServiciosDeInteres().stream().filter(parServicioRol -> parServicioRol.getServicio().equals(banioHombres)).findFirst().get();
    Assertions.assertEquals("AFECTADO",banioYrol.getRol().toString());
    miembro.cambiarRolSobreServicio(banioHombres);
    Assertions.assertEquals("OBSERVADOR",banioYrol.getRol().toString());
  }
}



