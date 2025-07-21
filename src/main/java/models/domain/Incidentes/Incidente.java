package models.domain.Incidentes;

import models.domain.Entidades.Establecimiento;
import models.domain.Personas.MiembroDeComunidad;
import models.persistence.Persistente;
import models.domain.Servicios.Servicio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="incidente")
public class Incidente extends Persistente {
    @ManyToOne
    @JoinColumn(name = "establecimiento_id", referencedColumnName = "id")
    private Establecimiento establecimiento;
    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;
    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "incidente_id", referencedColumnName = "id")
    private List<ReporteDeIncidente> reportes;

    public Incidente() {
        this.reportes = new ArrayList<>();
    }

    public List<ReporteDeIncidente> getReportesDeApertura() {
        return this.reportes.stream().filter(r -> !r.esDeCierre()).toList();
    }

    public List<ReporteDeIncidente> getReportesDeCierre() {
        return this.reportes.stream().filter(r -> r.esDeCierre()).toList();
    }

    public ReporteDeIncidente primeraApertura() {
        return this.getReportesDeApertura().get(0);
    }

    public ReporteDeIncidente primerCierre() {
        return this.getReportesDeCierre().get(0);
    }

    public void agregarReporteDeApertura(ReporteDeIncidente reporteDeIncidente) {
        // Se asume que los reportes están en orden cronológico
        if (this.getReportesDeApertura().isEmpty()) {
            this.establecimiento = reporteDeIncidente.getEstablecimiento();
            this.servicio = reporteDeIncidente.getServicio();
        }
        this.reportes.add(reporteDeIncidente);
    }

    public void agregarReporteDeCierre(ReporteDeIncidente reporteDeIncidente) {
        this.reportes.add(reporteDeIncidente);
    }

    public boolean cerrado() {
        return !(this.getReportesDeCierre().isEmpty());
    }

    public boolean tieneEstado(EstadoIncidente estadoIncidente) {
        return (this.cerrado() && estadoIncidente == EstadoIncidente.CERRADO) || (!this.cerrado() && estadoIncidente == EstadoIncidente.ABIERTO);
    }

    public Long tiempoDeCierre() {
        return ChronoUnit.MINUTES.between(this.primeraApertura().getFechaYhora(), this.getReportesDeCierre().get(0).getFechaYhora());
    } //lo hacemos en minutos y dsp se pasa a horas y minutos en el ranking

    public boolean igualito(Incidente incidente) {
        if (this == incidente) {
            return true;
        }
        if (incidente == null || getClass() != incidente.getClass()) {
            return false;
        }
        Incidente otro = incidente;
        return Objects.equals(establecimiento, otro.getEstablecimiento())
                && Objects.equals(servicio, otro.getServicio());
    }

    public Integer diasAbierto() {
        int dias = 1; //de base ya estuvo abierto ese mismo dia
        List<ReporteDeIncidente> reportesDeApertura = this.getReportesDeApertura();
        for (ReporteDeIncidente reporteDeIncidente : reportesDeApertura) {   //se fija si hay reportes que ocurrieron luego de 24 horas y que hayan sido anteriores al primer cierre
            //si no fue cerrado no se valida contra el de cierre

            if (this.cerrado())
                reportesDeApertura = reportesDeApertura.stream().filter(r -> !this.diferenciaMenor24Horas(r, reporteDeIncidente) && r.getFechaYhora().isBefore(this.primerCierre().getFechaYhora())).toList();
            else
                reportesDeApertura = reportesDeApertura.stream().filter(r -> !this.diferenciaMenor24Horas(r, reporteDeIncidente)).toList();

            if (!reportesDeApertura.isEmpty()) dias++;

        }
        return dias;
    }

    private boolean diferenciaMenor24Horas(ReporteDeIncidente reporteDeIncidente1, ReporteDeIncidente reporteDeIncidente2) {
        return Math.abs(ChronoUnit.HOURS.between(reporteDeIncidente1.getFechaYhora(), reporteDeIncidente2.getFechaYhora())) < 24;
    }

    public String mensaje() {
        return primeraApertura().mensaje();
    }

    public boolean fueAbiertoPor(MiembroDeComunidad miembroDeComunidad) {
        return !reportes.stream().filter(reporteDeIncidente -> !reporteDeIncidente.esDeCierre() && reporteDeIncidente.getDenunciante().equals(miembroDeComunidad)).toList().isEmpty();
    }

    public boolean fueCerradoPor(MiembroDeComunidad miembroDeComunidad) {
        return !reportes.stream().filter(reporteDeIncidente -> reporteDeIncidente.esDeCierre() && reporteDeIncidente.getDenunciante().equals(miembroDeComunidad)).toList().isEmpty();
    }

    public String tiempoAbierto() {
        Duration tiempoPasado;
        if(this.cerrado())
        {
            tiempoPasado = Duration.between(this.primeraApertura().getFechaYhora(), this.primerCierre().getFechaYhora());
        }else{
            tiempoPasado = Duration.between(this.primeraApertura().getFechaYhora(), LocalDateTime.now());
        }
        long dias = tiempoPasado.toDays();
        long horas = tiempoPasado.toHours() % 24;
        long minutos = tiempoPasado.toMinutes() % 60;
        if (dias != 1) {
            return dias + " dias " + horas + " horas " + minutos + " minutos";
        } else
            return dias + " dia " + horas + " horas " + minutos + " minutos";
    }

    public int cantVecesReportado(){
        return this.getReportesDeApertura().size();
    }

}