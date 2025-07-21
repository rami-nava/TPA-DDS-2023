package models.persistence.Repositorios;

import models.domain.Entidades.Entidad;

public class RepositorioEntidad extends RepositorioGenerico<Entidad> {
    private static RepositorioEntidad instancia = null;
    private RepositorioEntidad() {
        super(Entidad.class);
    }
    public static RepositorioEntidad getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioEntidad();
        }
        return instancia;
    }

}