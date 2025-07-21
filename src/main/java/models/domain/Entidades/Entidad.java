package models.domain.Entidades;

import models.persistence.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "entidad")
public class Entidad extends Persistente {
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="entidad_id")
    private List<Establecimiento> establecimientos;
    @Enumerated(EnumType.STRING)
    private TipoEntidad tipoEntidad;

    public Entidad() {
        this.establecimientos = new ArrayList<>();
    }
    public void agregarEstablecimiento(Establecimiento unEstablecimiento) {
        establecimientos.removeIf(establecimiento -> establecimiento.igualito(unEstablecimiento));
        establecimientos.add(unEstablecimiento);
    }

    public boolean igualito(Entidad entidad) {
        if (this == entidad) {
            return true;
        }
        if (entidad == null || getClass() != entidad.getClass()) {
            return false;
        }
        Entidad otro = entidad;
        return Objects.equals(nombre, otro.nombre)
            && Objects.equals(tipoEntidad, otro.tipoEntidad);
    }

    public int cantidadEstablecimientos(){
        return this.establecimientos.size();
    }
    public String tipo(){
        return this.tipoEntidad.toString();
    }

    public String nombreDeEstablecimientos(){ //Devuelve el nombre de los primeros 3 establecimientos, si es que tiene la menos 3, sino de los que haya
        String nombres = this.establecimientos.stream()
                                                .limit(3)
                                                .map(establecimiento -> establecimiento.getNombre())
                                                .collect(Collectors.joining(","));

        if(establecimientos.size() > 3)
            nombres = nombre + ", ...";

        return nombres;
    }

}
