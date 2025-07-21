package models.domain.Notificaciones;

import lombok.Getter;
import lombok.Setter;
import models.domain.Incidentes.ReporteDeIncidente;
import models.persistence.Persistente;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "NotificacionDeIncidente")
public class NotificacionDeIncidente extends Persistente {
  @ManyToOne
  @JoinColumn(name = "reporte_id", referencedColumnName = "id")
  private ReporteDeIncidente reporteDeIncidente;
  @Column(name = "destinatario")
  private String destinatario;
  @Column(name = "enviada")
  private Boolean enviada;

  public NotificacionDeIncidente(){
    this.enviada = false;
  }
}
