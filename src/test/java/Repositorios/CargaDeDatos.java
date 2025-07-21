package Repositorios;

import models.domain.Entidades.*;
import models.domain.Incidentes.EstadoIncidente;
import models.domain.Incidentes.Incidente;
import models.domain.Incidentes.ReporteDeIncidente;
import models.domain.Notificaciones.*;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Personas.Rol;
import models.domain.Rankings.EntidadesConMayorCantidadDeIncidentes;
import models.domain.Rankings.EntidadesQueSolucionanMasLento;
import models.domain.Servicios.Banio;
import models.domain.Servicios.Elevacion;
import models.domain.Servicios.Servicio;
import models.domain.Usuario.Usuario;
import models.persistence.EntityManagerSingleton;
import models.persistence.Repositorios.*;
import org.junit.jupiter.api.*;
import models.services.Localizacion.Municipio;
import models.services.Localizacion.Provincia;

import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.List;


public class CargaDeDatos {
    static RepositorioDeIncidentes repositorioDeIncidentes = RepositorioDeIncidentes.getInstancia();
    static RepositorioComunidad repositorioComunidad = RepositorioComunidad.getInstancia();
    static RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
    static RepositorioEntidad repositorioEntidad = RepositorioEntidad.getInstancia();
    static RepositorioDeReportesDeIncidentes repositorioDeReportesDeIncidentes = RepositorioDeReportesDeIncidentes.getInstancia();
    static RepositorioDeOrganismosDeControl repositorioDeOrganismosDeControl = RepositorioDeOrganismosDeControl.getInstancia();
    static RepositorioDeMunicipios repositorioDeMunicipios = RepositorioDeMunicipios.getInstancia();
    static RepositorioProvincias repositorioProvincias = RepositorioProvincias.getInstancia();
    static RepositorioDeEntidadPrestadora repositorioDeEntidadPrestadora = RepositorioDeEntidadPrestadora.getInstancia();
    static RepositorioDeEstablecimientos repositorioDeEstablecimientos = RepositorioDeEstablecimientos.getInstancia();
    static Municipio yavi = new Municipio();
    static Provincia jujuy = new Provincia();
    static Servicio banioUnisex;
    static Servicio escaleraMecanica;
    static Entidad lineaMitre;
    static Entidad lineaRoca;
    static EntidadPrestadora trenesArgentinos;
    static OrganismoDeControl CNRT;
    static Establecimiento estacionRetiro;
    static Establecimiento estacionVicenteLopez;
    static MiembroDeComunidad pablo;
    static Comunidad comunidad;
    static Comunidad comunidad2;
    static Usuario usuarioPablo;
    static ReporteDeIncidente incidenteBanioLineaMitre;
    static EntidadesQueSolucionanMasLento entidadesQueSolucionanMasLento = new EntidadesQueSolucionanMasLento();
    static EntidadesConMayorCantidadDeIncidentes entidadesConMayorCantidadDeIncidentes = new EntidadesConMayorCantidadDeIncidentes();
    static EntityTransaction tx;
    static MedioDeComunicacion mail = new ViaMail();
    static FormaDeNotificar cuandoSuceden = new CuandoSuceden();
    @BeforeAll
    public static void before() {

        tx =  EntityManagerSingleton.getInstance().getTransaction();
        tx.begin();

        jujuy = new Provincia();
        yavi = new Municipio();
        banioUnisex = new Banio();
        escaleraMecanica = new Elevacion();
        estacionRetiro = new Establecimiento();
        estacionVicenteLopez = new Establecimiento();
        lineaMitre = new Entidad();
        lineaRoca = new Entidad();
        trenesArgentinos = new EntidadPrestadora();
        CNRT = new OrganismoDeControl();
        comunidad = new Comunidad();
        comunidad2 = new Comunidad();
        pablo = new MiembroDeComunidad();
        usuarioPablo = new Usuario();

        //Cargo provincia
        jujuy.setId(38);
        jujuy.setNombre("Jujuy");
        repositorioProvincias.agregar(jujuy);

        //Cargo municipio
        yavi.setId(386273);
        yavi.setNombre("Yavi");
        yavi.setProvincia(jujuy);
        repositorioDeMunicipios.agregar(yavi);

        //Cargo Servicio
        banioUnisex.setTipo("UNISEX");
        escaleraMecanica.setTipo("ESCALERAS_MECANICAS");


        //Cargo Establecimiento
        estacionRetiro.setLocalizacion(yavi);
        estacionRetiro.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
        estacionRetiro.setNombre("Retiro");
        estacionRetiro.agregarServicio(banioUnisex);
        estacionVicenteLopez.setLocalizacion(yavi);
        estacionVicenteLopez.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
        estacionVicenteLopez.setNombre("Vicente Lopez");
        estacionRetiro.agregarServicio(escaleraMecanica);
        repositorioDeEstablecimientos.agregar(estacionRetiro);
        repositorioDeEstablecimientos.agregar(estacionVicenteLopez);

        //Cargo Entidad
        lineaMitre.setTipoEntidad(TipoEntidad.FERROCARRIL.FERROCARRIL);
        lineaMitre.setNombre("Linea Mitre");
        lineaMitre.agregarEstablecimiento(estacionRetiro);
        lineaRoca.setTipoEntidad(TipoEntidad.FERROCARRIL.FERROCARRIL);
        lineaRoca.setNombre("Linea Roca");
        lineaRoca.agregarEstablecimiento(estacionVicenteLopez);
        repositorioEntidad.agregar(lineaMitre);
        repositorioEntidad.agregar(lineaRoca);

        //Cargo Entidad Prestadora
        trenesArgentinos.setNombre("Trenes Argentinos");
        trenesArgentinos.setPersonaMail("fanDeTrenes@gmail.com");
        trenesArgentinos.agregarEntidad(lineaMitre);
        trenesArgentinos.agregarEntidad(lineaRoca);
        repositorioDeEntidadPrestadora.agregar(trenesArgentinos);

        //Cargo Organismos de Control
        CNRT.setNombre("CNRT");
        CNRT.setPersonaMail("encargadoCNRT@gmail.com");
        CNRT.agregarEntidadPrestadora(trenesArgentinos);
        repositorioDeOrganismosDeControl.agregar(CNRT);

        //Cargo Comunidades
        comunidad.setNombre("Los+Capos");
        comunidad2.setNombre("Los+Piolas");

        repositorioComunidad.agregar(comunidad);
        repositorioComunidad.agregar(comunidad2);

        //Cargo Miembros de Comunidad
        pablo.setNombre("pablo");
        pablo.setApellido("perez");
        ReceptorDeNotificaciones receptorDeNotificaciones = pablo.getReceptorDeNotificaciones();
        receptorDeNotificaciones.cambiarFormaDeNotificar(cuandoSuceden);
        receptorDeNotificaciones.cambiarMedioDeComunicacion(mail);
        receptorDeNotificaciones.setMail("hola@mail.net");
        receptorDeNotificaciones.setTelefono("+1333333453");
        receptorDeNotificaciones.cambiarFormaDeNotificar(new CuandoSuceden());
        receptorDeNotificaciones.cambiarMedioDeComunicacion(new ViaMail());
        pablo.setReceptorDeNotificaciones(receptorDeNotificaciones);

        pablo.agregarServicioDeInteres(banioUnisex, Rol.valueOf("AFECTADO"));

        pablo.unirseAComunidad(comunidad);
        pablo.unirseAComunidad(comunidad2);

        pablo.agregarEntidadDeInteres(lineaMitre);

        usuarioPablo.setUsername("xPablox");
        usuarioPablo.cambiarContrasenia("pablitoElMa$Cap@DeT@d@s");

        pablo.setUsuario(usuarioPablo);

        repositorioMiembroDeComunidad.agregar(pablo);

        //Cargo los Reportes de Incidentes
        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.ABIERTO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,22,19,30,30));
        incidenteBanioLineaMitre.setObservaciones("Baño inundado, todo el piso mojado");

        //pablo.informarFuncionamiento(incidenteBanioLineaMitre,pablo.getComunidades().get(0));

        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.ABIERTO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,22,19,30,30));
        incidenteBanioLineaMitre.setObservaciones("Baño inundado, todo el piso mojado");

        guardarIncidenteComunidad(comunidad, incidenteBanioLineaMitre);
        repositorioDeReportesDeIncidentes.agregar(incidenteBanioLineaMitre);

        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.ABIERTO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,22,19,45,30));
        incidenteBanioLineaMitre.setObservaciones("Baño inundado, todo el piso mojado");

        guardarIncidenteComunidad(comunidad2, incidenteBanioLineaMitre);
        repositorioDeReportesDeIncidentes.agregar(incidenteBanioLineaMitre);

        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.CERRADO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,22,21,45,30));
        incidenteBanioLineaMitre.setObservaciones("Baño ya fue limpiado");

        guardarIncidenteComunidad(comunidad, incidenteBanioLineaMitre);
        repositorioDeReportesDeIncidentes.agregar(incidenteBanioLineaMitre);

        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.CERRADO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,22,21,50,30));
        incidenteBanioLineaMitre.setObservaciones("Baño ya fue limpiado");

        guardarIncidenteComunidad(comunidad2, incidenteBanioLineaMitre);
        repositorioDeReportesDeIncidentes.agregar(incidenteBanioLineaMitre);

        incidenteBanioLineaMitre = new ReporteDeIncidente();
        incidenteBanioLineaMitre.setDenunciante(pablo);
        incidenteBanioLineaMitre.setClasificacion(EstadoIncidente.ABIERTO);
        incidenteBanioLineaMitre.setEntidad(lineaMitre);
        incidenteBanioLineaMitre.setEstablecimiento(estacionRetiro);
        incidenteBanioLineaMitre.setServicio(banioUnisex);
        incidenteBanioLineaMitre.setFechaYhora(LocalDateTime.of(2023,9,23,10,45,30));
        incidenteBanioLineaMitre.setObservaciones("Volvieron a mojar el baño");

        guardarIncidenteComunidad(comunidad, incidenteBanioLineaMitre);
        repositorioDeReportesDeIncidentes.agregar(incidenteBanioLineaMitre);
    }
    @AfterAll
    public static void after() {
        tx.rollback();
    }

    @Test
    public void testBD() {
        try {
            this.metodoBD();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.tx.rollback();
        }
    }
        public void metodoBD(){



            Assertions.assertTrue(true);
        }
        @Test
        public void leerBaseDeDatos(){
            System.out.println(repositorioMiembroDeComunidad.buscarTodos().get(0));
        }

        @Test
        public void rankingSolucionanMasLento() {
            entidadesQueSolucionanMasLento.armarRanking(repositorioEntidad.buscarTodos(),repositorioDeIncidentes.getIncidentesEstaSemana());
        }

        @Test
        public void rankingMayorCantidadDeIncidentes() {
            entidadesConMayorCantidadDeIncidentes.armarRanking(repositorioEntidad.buscarTodos(),repositorioDeIncidentes.getIncidentesEstaSemana());
        }

        @Test
        public void solicitarInformacionDeIncidentesAbiertos(){
            List<MiembroDeComunidad> miembroDeComunidads = repositorioMiembroDeComunidad.buscarTodos();
            MiembroDeComunidad pablito = miembroDeComunidads.get(0);
            Assertions.assertEquals(1,pablito.obtenerIncidentesPorEstado(EstadoIncidente.ABIERTO, repositorioDeIncidentes.buscarTodos()).size());
        }
        @Test
        public void solicitarInformacionDeIncidentesCerrados(){
            MiembroDeComunidad pablito = repositorioMiembroDeComunidad.buscarTodos().get(0);

            Assertions.assertEquals(1,pablito.obtenerIncidentesPorEstado(EstadoIncidente.CERRADO, repositorioDeIncidentes.buscarTodos()).size());
        }


    public static void guardarIncidenteComunidad(Comunidad comunidad, ReporteDeIncidente reporteDeIncidente) {
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

}