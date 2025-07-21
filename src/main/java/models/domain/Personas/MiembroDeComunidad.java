package models.domain.Personas;

import models.domain.Entidades.Entidad;
import models.domain.Entidades.Establecimiento;
import models.domain.Incidentes.EstadoIncidente;
import models.domain.Incidentes.Incidente;
import models.domain.Incidentes.ReporteDeIncidente;
import models.domain.Notificaciones.ReceptorDeNotificaciones;
import models.persistence.Persistente;
import models.domain.Servicios.Servicio;
import models.domain.Usuario.Usuario;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name="miembroDeComunidad")
public class MiembroDeComunidad extends Persistente {
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "puntosDeConfianza")
    private Long puntosDeConfianza;
    @ManyToMany
    private List<Entidad> entidadesDeInteres;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "miembro_id", referencedColumnName = "id")
    private List<ParServicioRol> serviciosDeInteres;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "usuario_id", referencedColumnName = "id") // Asegura que la columna sea única
    private Usuario usuario;
    @ManyToMany
    private List<Comunidad> comunidades;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private ReceptorDeNotificaciones receptorDeNotificaciones;

    public MiembroDeComunidad() {
        this.entidadesDeInteres = new ArrayList<>();
        this.serviciosDeInteres = new ArrayList<>();
        this.comunidades = new ArrayList<>();
        this.receptorDeNotificaciones = new ReceptorDeNotificaciones(); //para que se persiste automaticamente
    }

    public void agregarEntidadDeInteres(Entidad entidad) {
        entidadesDeInteres.add(entidad);
    }

    public void agregarServicioDeInteres(Servicio servicio, Rol rol) {
        ParServicioRol parServicioRol = new ParServicioRol();
        parServicioRol.setServicio(servicio);
        parServicioRol.setRol(rol);
        serviciosDeInteres.add(parServicioRol);
    }
    public void cambiarRolSobreServicio(Servicio servicio){
        Optional<ParServicioRol> parServicioRolExistente = this.serviciosDeInteres.stream().filter(parServicioRol -> parServicioRol.getServicio().equals(servicio)).findFirst();
        if(parServicioRolExistente.isPresent())
            parServicioRolExistente.get().cambiarRol();//de esta forma no creamos una nueva instancia sino que la modificamos
    }

    public void unirseAComunidad(Comunidad unaComunidad) {
        this.comunidades.add(unaComunidad);
        unaComunidad.agregarMiembro(this);
    }

    public boolean tieneInteres(Servicio servicio, Establecimiento establecimiento) {
        boolean coincideEstablecimiento = this.entidadesDeInteres.stream().anyMatch(entidad -> entidad.getEstablecimientos().contains(establecimiento));
        boolean coincideLocalizacion = true;
        boolean coincideServicio = this.serviciosDeInteres.stream().anyMatch(servicioRol -> servicioRol.getServicio().equals(servicio));
        return coincideServicio && coincideEstablecimiento && coincideLocalizacion;
    }

    public void recibirNotificacion(ReporteDeIncidente reporteDeIncidente) {
        Servicio servicio = reporteDeIncidente.getServicio();
        Establecimiento establecimiento = reporteDeIncidente.getEstablecimiento();

        if (this.tieneInteres(servicio, establecimiento)) {
            this.receptorDeNotificaciones.recibirNotificacion(reporteDeIncidente);
        }
    }
    public List<Incidente> obtenerIncidentesPorEstado(EstadoIncidente estado, List<Incidente> incidentes) {
        //Se queda con los incidentes que pertenezcan por lo menos a una de sus comunidades
        //Estos incidentes no estaran repetidos, seran unicos
        List<Incidente> incidentesDeMisComunidades = incidentes.stream().filter(incidente -> this.comunidades.stream().anyMatch(comunidad -> comunidad.incidenteEsDeComunidad(incidente))).toList();

        List<Incidente> incidentesDeEstadoSeleccionado = incidentesDeMisComunidades.stream().filter(i -> i.tieneEstado(estado)).toList();

        return incidentesDeEstadoSeleccionado;
    }

    public List<Incidente> solicitarInformacionDeIncidentesAbiertos(List<Incidente> incidentes) {
        return obtenerIncidentesPorEstado(EstadoIncidente.ABIERTO, incidentes);
    }

    public List<Incidente> solicitarInformacionDeIncidentesCerrados(List<Incidente> incidentes) {
        return obtenerIncidentesPorEstado(EstadoIncidente.CERRADO, incidentes);
    }

    public boolean afectadoPor(Incidente incidente) {
        boolean tieneRolAfectado = this.serviciosDeInteres.stream().anyMatch(servicioRol -> servicioRol.getServicio().igualito(incidente.getServicio()));
        return this.tieneInteres(incidente.getServicio(), incidente.getEstablecimiento()) && tieneRolAfectado;
    }

    public void agregarComunidad(Comunidad comunidad) {
        this.comunidades.add(comunidad);
    }

  public void abandonarComunidad(Comunidad comunidadAEliminar) {
        this.comunidades.remove(comunidadAEliminar);
  }

  public boolean esEntidadDeInteres(Entidad entidad){
        return entidadesDeInteres.contains(entidad);
  }

  public void eliminarEntidadDeInteres(Entidad entidadAEliminar) {
        entidadesDeInteres.remove(entidadAEliminar);
  }

  public boolean esServicioDeInteres(Servicio servicio) {
      return this.serviciosDeInteres.stream().anyMatch(servicioRol -> servicioRol.getServicio().equals(servicio));
  }

  public void eliminarServicioDeInteres(ParServicioRol parServicioRol) {
      this.serviciosDeInteres.remove(parServicioRol);
    }

  public boolean esObservador(Servicio servicio){
        return !this.serviciosDeInteres.stream().filter(parServicioRol ->
                parServicioRol.getServicio().equals(servicio) &&
                parServicioRol.getRol().equals(Rol.OBSERVADOR)).toList().isEmpty();
  }

    public boolean esAfectado(Servicio servicio){
        return !this.serviciosDeInteres.stream().filter(parServicioRol ->
                parServicioRol.getServicio().equals(servicio) &&
                parServicioRol.getRol().equals(Rol.AFECTADO)).toList().isEmpty();
    }

}


