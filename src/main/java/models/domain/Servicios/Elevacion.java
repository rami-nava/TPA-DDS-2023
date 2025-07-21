package models.domain.Servicios;


import javax.persistence.*;
import java.util.Objects;
@Entity
@DiscriminatorValue("elevacion")
public class Elevacion extends Servicio{
    @Enumerated(EnumType.STRING)
    private TipoElevacion tipoElevacion;

    @Override
    public void setTipo(String tipo) {
        tipoElevacion = TipoElevacion.valueOf(tipo);
        tipoNombre = this.tipoNombre();
    }
    public boolean estaActivo() {
        //TODO
        return true;
    }
    public String getTipo(){
        return String.valueOf(tipoElevacion);
    }

    public String tipoNombre(){
        switch (tipoElevacion){
            case ASCENSOR: return "Ascensor";
            case ESCALERAS_MECANICAS: return "Escaleras Mecanicas";
        }
        return null;
    }
    public Elevacion() {
        this.nombre = this.nombre();
    }
    public boolean igualito(Elevacion elevacion) {
        if (this == elevacion) {
            return true;
        }
        if (elevacion == null || getClass() != elevacion.getClass()) {
            return false;
        }
        Elevacion otro = elevacion;
        return tipoElevacion == otro.tipoElevacion;
    }
    @Override
    public int hashCode() {
        return Objects.hash(tipoElevacion);
    }
}
