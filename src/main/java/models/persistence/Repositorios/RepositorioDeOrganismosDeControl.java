package models.persistence.Repositorios;

import models.domain.Entidades.OrganismoDeControl;

public class RepositorioDeOrganismosDeControl extends RepositorioGenerico<OrganismoDeControl> {
    private static RepositorioDeOrganismosDeControl instancia = null;

    private RepositorioDeOrganismosDeControl() {
        super(OrganismoDeControl.class);
    }

    public static RepositorioDeOrganismosDeControl getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeOrganismosDeControl();
        }
        return instancia;
    }
}
