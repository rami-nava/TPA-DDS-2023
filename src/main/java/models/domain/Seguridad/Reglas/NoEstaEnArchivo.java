package models.domain.Seguridad.Reglas;

import models.domain.Seguridad.NoSeEncontroElArchivo;
import models.services.Archivos.SistemaDeArchivos;

import java.io.IOException;

public class NoEstaEnArchivo implements ReglaContrasenia{
    private String ruta;

    public NoEstaEnArchivo(String ruta) {
        this.ruta = ruta;
    }

    public boolean cumpleRegla(String contrasenia)  {
        try {
            SistemaDeArchivos sistemaDeArchivos = new SistemaDeArchivos();
            return !sistemaDeArchivos.estaEnArchivo(contrasenia, ruta);
        } catch (IOException e) {
            throw new NoSeEncontroElArchivo("No se encontro el archivo");
        }
    }
}
