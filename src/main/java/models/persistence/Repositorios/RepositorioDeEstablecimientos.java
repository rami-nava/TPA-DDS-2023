package models.persistence.Repositorios;

import models.domain.Entidades.Establecimiento;

public class RepositorioDeEstablecimientos extends RepositorioGenerico<Establecimiento> {
    private static RepositorioDeEstablecimientos instancia = null;

    private RepositorioDeEstablecimientos() {
        super(Establecimiento.class);
    }

    public static RepositorioDeEstablecimientos getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeEstablecimientos();
        }
        return instancia;
    }
}
