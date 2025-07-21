package models.persistence.Repositorios;

import models.domain.Entidades.EntidadPrestadora;

public class RepositorioDeEntidadPrestadora extends RepositorioGenerico<EntidadPrestadora> {
    private static RepositorioDeEntidadPrestadora instancia = null;
    private RepositorioDeEntidadPrestadora() {
        super(EntidadPrestadora.class);
    }
    public static  RepositorioDeEntidadPrestadora getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeEntidadPrestadora();
        }
        return instancia;
    }

}
