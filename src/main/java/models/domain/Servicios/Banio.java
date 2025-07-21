package models.domain.Servicios;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@DiscriminatorValue("banio")
public class Banio extends Servicio{
    @Enumerated(EnumType.STRING)
    @Setter
    private TipoBanio tipoBanio;

    public Banio () {
        this.nombre = this.nombre();
    };
    public boolean estaActivo() {
        //TODO
        return true;
    }
    @Override
    public void setTipo(String tipo) {
        tipoBanio = TipoBanio.valueOf(tipo);
        tipoNombre = this.tipoNombre();
    }
    public String getTipo(){
        return "Banio " + String.valueOf(tipoBanio);
    }
    public String tipoNombre(){
        switch (tipoBanio){
            case CABALLEROS: return "Caballeros";
            case DAMAS: return "Damas";
            case UNISEX: return "Unisex";
        }
        return null;
    }


    public boolean igualito(Servicio banio) {
        if (this == banio) {
            return true;
        }
        if (banio == null || getClass() != banio.getClass()) {
            return false;
        }
        Banio otro = (Banio) banio;
        return tipoBanio == otro.tipoBanio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipoBanio);
    }
}

