package models.services.Localizacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Table(name="municipio")
public class Municipio {
    @Id
    //private int identificador
    @NaturalId
    public int id;
    @Column(name = "nombre")
    public String nombre;
    @ManyToOne //CORREGIR APARECEN COLUMNAS DE MAS EN AMBAS TABLAS (1 Y 1)
    @JoinColumn(name="provincia_id",referencedColumnName = "id")
    public Provincia provincia;

    public Municipio() {}
}