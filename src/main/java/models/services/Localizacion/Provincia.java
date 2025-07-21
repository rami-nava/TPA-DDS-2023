package models.services.Localizacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Table(name = "provincia")
public class Provincia {
    @Id
    //private int identificador;
    @NaturalId
    public int id;
    @Column(name = "nombre")
    public String nombre;

    public Provincia() {}
}