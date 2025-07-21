package models.domain.Personas;

import models.persistence.Persistente;
import models.domain.Servicios.Servicio;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "parServicioRol")
public class ParServicioRol extends Persistente {
    @ManyToOne
    @JoinColumn(name = "servicio_id", referencedColumnName = "id")
    private Servicio servicio;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public ParServicioRol() {}

    public void cambiarRol(){
        if(this.rol == Rol.OBSERVADOR)
            this.rol = Rol.AFECTADO;
        else
            this.rol = Rol.OBSERVADOR;
    }
    public String nombreRol(){
        switch (rol){
            case OBSERVADOR: return "Observador";
            case AFECTADO: return "Afectado";
            default: return "Rol no reconocido";
        }
    }

    public String otroRol(){
        switch (rol){
            case OBSERVADOR: return "Afectado";
            case AFECTADO: return "Observador";
            default: return "Rol actual no reconocido";
        }
    }
}
