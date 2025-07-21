package models.domain.Personas;

import models.domain.Incidentes.Incidente;
import models.domain.Incidentes.ReporteDeIncidente;
import models.persistence.Persistente;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="comunidad")
public class Comunidad extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "gradosDeConfianza")
    private String gradosDeConfianza;
    @ManyToMany(mappedBy = "comunidades")
    private List<MiembroDeComunidad> miembros;
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    @JoinColumn(name = "comunidad_id")
    private List<ReporteDeIncidente> reportesDeLaComunidad;


    public Comunidad() {
        this.miembros = new ArrayList<>();
        this.reportesDeLaComunidad = new ArrayList<>();
    }
    public void agregarMiembro(MiembroDeComunidad unMiembro) {
        this.miembros.add(unMiembro);
    }
    public boolean cerroIncidente(Incidente incidente) {
        return this.reportesDeLaComunidad.stream().anyMatch(r -> incidente.getReportesDeCierre().contains(r));
    }
    public boolean abrioIncidente(Incidente incidente) {
        return this.reportesDeLaComunidad.stream().anyMatch(r -> incidente.getReportesDeApertura().contains(r));
    }
    public List<Incidente> getIncidentesDeComunidad(List<Incidente> incidentes) {
        return incidentes.stream().filter(i -> this.incidenteEsDeComunidad(i)).toList();
    }
    public boolean incidenteEsDeComunidad(Incidente incidente) {
        return this.reportesDeLaComunidad.stream().anyMatch(r -> incidente.getReportesDeApertura().contains(r));
    }
    public void agregarReporte(ReporteDeIncidente reporteDeIncidente){
        this.reportesDeLaComunidad.add(reporteDeIncidente);
    }
    public List<Incidente> incidentesAbiertos(List<Incidente> incidentes){
        return this.getIncidentesDeComunidad(incidentes).stream().filter(i -> !i.cerrado()).toList();
    }

    public int cantidadMiembros(){
        return  this.miembros.size();
    }

    public MiembroDeComunidad administrador(){
        if(!miembros.isEmpty())
            return miembros.get(0);
        else
            return null;
    }

    public List<MiembroDeComunidad> miembrosNoAdministradores(){
        return miembros.subList(1,miembros.size());
    }

    public int cantIncidentesReportados(){
        return this.reportesDeLaComunidad.stream().filter(reporteDeIncidente -> !reporteDeIncidente.esDeCierre()).toList().size();
    }

    public int cantIncidentesCerrados(){
        return this.reportesDeLaComunidad.stream().filter(reporteDeIncidente -> reporteDeIncidente.esDeCierre()).toList().size();
    }

}