package models.domain.Entidades;


import models.domain.Notificaciones.ViaMail;
import models.persistence.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "entidadPrestadora")
public class EntidadPrestadora extends Persistente {
  @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinColumn(name="entidadprestadora_id")
  private List<Entidad> entidades;
  @Column(name = "nombre")
  @Setter
  private String nombre;
  @Column(name = "personaMail")
  @Setter
  private String personaMail;
  @Transient
  private ViaMail viaMail;
  public EntidadPrestadora(){
    this.entidades = new ArrayList<>();
    this.viaMail = new ViaMail();
  }
  public void agregarEntidad(Entidad entidad){
    entidades.removeIf(entidad1 -> entidad1.getNombre().equals(entidad.getNombre()));
    entidades.add(entidad);
  }

  public boolean igualito(EntidadPrestadora entidadPrestadora) {
    if (this == entidadPrestadora) {
      return true;
    }
    if (entidadPrestadora == null || getClass() != entidadPrestadora.getClass()) {
      return false;
    }
    EntidadPrestadora otro = entidadPrestadora;
    return Objects.equals(nombre, otro.nombre);
  }

  public void recibirInforme(String ruta,String asunto){
    if (this.personaMail != null)
      this.viaMail.recibirArchivos(ruta,asunto,this.personaMail);
  }

  public int cantidadEntidades(){
    return this.entidades.size();
  }

  public boolean tieneContacto(){
    return personaMail != null;
  }
  public String nombreDeEntidades(){ //Devuelve el nombre de las primeras 3 entidades, si es que tiene la menos 3, sino de los que haya
    String nombres = this.entidades.stream()
        .limit(3)
        .map(entidad -> entidad.getNombre())
        .collect(Collectors.joining(","));

    if(entidades.size() > 3)
      nombres = nombre + ", ...";

    return nombres;
  }
}
