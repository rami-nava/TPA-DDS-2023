package models.domain.Servicios;

import lombok.Getter;
import lombok.Setter;
import models.domain.Entidades.Establecimiento;
import models.persistence.Persistente;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "servicio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Servicio extends Persistente {
  protected String nombre;
  protected String tipoNombre;
  @Setter
  @Getter
  @Transient
  protected Establecimiento establecimiento;
  public abstract boolean estaActivo();
  public abstract String getTipo();
  public abstract String tipoNombre();

  public String nombre(){
    return this.getClass().getSimpleName();
  }

  public String estado(){
    if(estaActivo())
      return "En funcionamiento";
    else
      return "Deshabilitado";
  }

  public abstract void setTipo(String tipo);
  public boolean igualito(Servicio servicio) {
    if (this == servicio) {
      return true;
    }
    if (servicio == null || getClass() != servicio.getClass()) {
      return false;
    }
    Servicio otro = servicio;
    return Objects.equals(getTipo(),otro.getTipo())
        && Objects.equals(getClass(),otro.getClass());
  }

  public boolean esBanio(){
    return this.getClass().getSimpleName().equals("Banio");
  }
}

