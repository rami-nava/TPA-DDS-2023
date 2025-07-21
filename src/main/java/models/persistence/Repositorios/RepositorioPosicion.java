package models.persistence.Repositorios;

import models.domain.Entidades.Posicion;


public class RepositorioPosicion extends RepositorioGenerico<Posicion> {
    private static RepositorioPosicion instancia = null;

    private RepositorioPosicion() {
        super(Posicion.class);
    }
    public static RepositorioPosicion getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioPosicion();
        }
        return instancia;
    }
}