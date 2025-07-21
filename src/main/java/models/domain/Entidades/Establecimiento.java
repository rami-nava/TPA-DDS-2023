package models.domain.Entidades;

import models.persistence.Persistente;
import models.domain.Servicios.Servicio;
import lombok.Getter;
import lombok.Setter;
import models.services.Localizacion.Municipio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(name="establecimiento")
public class Establecimiento extends Persistente {
  @Column(name = "nombre")
  @Setter
  private String nombre;
  @Enumerated(EnumType.STRING)
  @Setter
  private TipoEstablecimiento tipoEstablecimiento;
  @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name="establecimiento_id")
  private List<Servicio> servicios;
  @ManyToOne
  @JoinColumn(name="municipio_id",referencedColumnName = "id")
  @Setter
  private Municipio localizacion;
  @ManyToOne
  @JoinColumn(name = "posicion_id", referencedColumnName = "id")
  @Setter
  private Posicion posicion;

  public Establecimiento() {
    this.servicios = new ArrayList<>();
  }
  public boolean estaEnFuncionamiento(Servicio servicio) { // X CANTIDAD DE TIEMPO
    if (servicios.contains(servicio)) {
      return servicio.estaActivo();
    }
    return false;
  }
  public void agregarServicio(Servicio servicio) {
    this.servicios.add(servicio);
  }
  public boolean igualito(Establecimiento establecimiento) {
    if (this == establecimiento) {
      return true;
    }
    if (establecimiento == null || getClass() != establecimiento.getClass()) {
      return false;
    }
    Establecimiento otro = establecimiento;
    return Objects.equals(nombre, otro.nombre)
        && Objects.equals(tipoEstablecimiento,otro.tipoEstablecimiento)
        && Objects.equals(localizacion.getId(),otro.localizacion.getId());
  }

  public String tipo() {
    String tipo = this.tipoEstablecimiento.toString().toLowerCase();
    return tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
  }

  public int cantidadServicios(){
    return this.servicios.size();
  }

}
