package models.domain.Usuario;

import lombok.Getter;
import lombok.Setter;
import models.persistence.Persistente;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="permiso")
public class Permiso extends Persistente {
  @Column(name = "nombre")
  private String nombre;

  @Column(name = "nombreInterno")
  private String nombreInterno;

  public boolean coincideConNombreInterno(String nombre) {
    return this.nombreInterno.equals(nombre);
  }

}
