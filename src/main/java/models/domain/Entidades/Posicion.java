package models.domain.Entidades;

import models.persistence.Persistente;
import lombok.Getter;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "posicion")
public class Posicion extends Persistente {
    @Column(name = "latitud")
    private double latitud;
    @Column(name = "longitud")
    private double longitud;
    public Posicion() {}
    public double distancia(Posicion posicion) {
        return Math.sqrt(Math.pow(this.latitud-posicion.getLatitud(), 2)+Math.pow(this.longitud-posicion.getLongitud(), 2));
    }
    public void setPosicion(String coordenadas) { //Ingresa en formato "40.7486,-73.9864"
        String[] aux = coordenadas.split(",");
        this.latitud = Double.valueOf(aux[0]);
        this.longitud = Double.valueOf(aux[1]);
    }

    public double calcularDistancia(Posicion posicion) {
        double radioTierra = 6371; // Radio de la Tierra en kilómetros
        double dLat = gradosARadianes(this.latitud - posicion.getLatitud());
        double dLon = gradosARadianes(this.longitud - posicion.getLongitud());
        double a =      Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(gradosARadianes(posicion.getLatitud()))
                        * Math.cos(gradosARadianes(posicion.getLongitud()))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = radioTierra * c;
        return distancia;
    }

    public static double gradosARadianes(double grados) {
        return grados * (Math.PI / 180);
    }
}
