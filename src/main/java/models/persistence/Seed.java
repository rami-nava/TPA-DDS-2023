package models.persistence;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import models.domain.Notificaciones.CuandoSuceden;
import models.domain.Notificaciones.ReceptorDeNotificaciones;
import models.domain.Notificaciones.ViaMail;
import models.domain.Personas.MiembroDeComunidad;
import models.domain.Usuario.Rol;
import models.domain.Usuario.TipoRol;
import models.domain.Usuario.Usuario;
import models.persistence.Repositorios.*;
import models.services.APIs.Georef.ServicioGeoref;
import models.services.Localizacion.ListadoDeProvincias;

import javax.persistence.*;



public class Seed implements WithSimplePersistenceUnit {
    public static void main(String[] args) {
        seed();
    }
    public static void seed() {
        EntityManager em = EntityManagerSingleton.getInstance();
        ServicioGeoref servicioGeoref = ServicioGeoref.instancia();
        RepositorioDeMunicipios repositorioDeMunicipios = RepositorioDeMunicipios.getInstancia();
        RepositorioProvincias repositorioProvincias = RepositorioProvincias.getInstancia();
        ListadoDeProvincias listadoDeProvinciasArgentinas = servicioGeoref.listadoDeProvincias();


        try {
            em.getTransaction().begin();

            em.createNativeQuery("DROP TABLE IF EXISTS rol_permiso").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS miembroDeComunidad_comunidad").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS miembroDeComunidad_entidad").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS miembroDeComunidad_municipio").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS miembroDeComunidad_provincia").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS NotificacionDeIncidente").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS reporteDeIncidente").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS parServicioRol").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS miembroDeComunidad").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS usuario").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS rol").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS comunidad").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS incidente").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS servicio").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS establecimiento").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS entidad").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS entidadPrestadora").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS organismoDeControl").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS municipio").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS permiso").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS posicion").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS provincia").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS receptorDeNotificaciones").executeUpdate();
            em.createNativeQuery("DROP TABLE IF EXISTS hibernate_sequence").executeUpdate();

            em.getTransaction().commit();
            em.close();

            em = EntityManagerSingleton.getInstance();
            em.getTransaction().begin();

            //Se cargan las provincias
            listadoDeProvinciasArgentinas.getProvincias().forEach(provincia -> repositorioProvincias.agregar(provincia));

            //Se cargan los municipios
            repositorioProvincias.buscarTodos().forEach(
                    provincia -> servicioGeoref.listadoDeMunicipiosDeProvincia(provincia).
                            getMunicipios().forEach(municipio -> repositorioDeMunicipios.agregar(municipio)));

            //PARA PROBAR LA PAGINA WEB
            RepositorioDeUsuarios repositorioDeUsuarios = RepositorioDeUsuarios.getInstancia();
            RepositorioRoles repositorioRoles = RepositorioRoles.getInstancia();
            Usuario u1 = new Usuario();
            u1.setUsername("messi10");
            u1.setContrasenia("Grupodisenio@");

            Rol admin = new Rol();
            admin.setNombre("administrador");
            admin.setTipo(TipoRol.ADMINISTRADOR);
            Rol basico = new Rol();
            basico.setNombre("basico");
            basico.setTipo(TipoRol.USUARIO_BASICO);
            Rol empresa = new Rol();
            empresa.setNombre("empresa");
            empresa.setTipo(TipoRol.USUARIO_EMPRESA);

            repositorioRoles.agregar(admin);
            repositorioRoles.agregar(basico);
            repositorioRoles.agregar(empresa);

            u1.setRol(admin);

            repositorioDeUsuarios.agregar(u1);

            RepositorioMiembroDeComunidad repositorioMiembroDeComunidad = RepositorioMiembroDeComunidad.getInstancia();
            MiembroDeComunidad mi = new MiembroDeComunidad();
            mi.setApellido("Messi");
            mi.setNombre("Leo");
            mi.setUsuario(u1);

            ReceptorDeNotificaciones receptorDeNotificaciones1 = new ReceptorDeNotificaciones();
            receptorDeNotificaciones1.setMail("diseniogrupo9@gmail.com");
            receptorDeNotificaciones1.setTelefono("01144444444");
            receptorDeNotificaciones1.cambiarFormaDeNotificar(new CuandoSuceden());
            receptorDeNotificaciones1.cambiarMedioDeComunicacion(new ViaMail());

            mi.setReceptorDeNotificaciones(receptorDeNotificaciones1);
            mi.setUsuario(u1);

            repositorioMiembroDeComunidad.agregar(mi);

            em.getTransaction().commit();
            em.close();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}



