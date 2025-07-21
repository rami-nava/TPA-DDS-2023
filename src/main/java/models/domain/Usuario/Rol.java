package models.domain.Usuario;

import lombok.Getter;
import lombok.Setter;
import models.persistence.Persistente;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="rol")
public class Rol extends Persistente {

  @Column(name = "nombre")
  private String nombre;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo")
  private TipoRol tipo;

  @ManyToMany
  private Set<Permiso> permisos;

  public Rol() {
    this.permisos = new HashSet<>();
  }

}
