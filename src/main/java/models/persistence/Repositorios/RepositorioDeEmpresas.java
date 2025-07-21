package models.persistence.Repositorios;

import models.Config.Config;
import models.domain.Entidades.OrganismoDeControl;
import lombok.Getter;
import models.services.APIs.Georef.ServicioGeoref;
import models.services.Archivos.CargadorDeDatos;
import models.services.Archivos.SistemaDeArchivos;

import java.util.List;

@Getter
public class RepositorioDeEmpresas {

    private static RepositorioDeEmpresas instancia = null;
    private RepositorioDeEmpresas() {}

    public static  RepositorioDeEmpresas getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeEmpresas();
        }
        return instancia;
    }
    public static void main(String[] args) {

        List<OrganismoDeControl> empresas;

        CargadorDeDatos cargadorDeDatos = new CargadorDeDatos();
        SistemaDeArchivos sistemaDeArchivos = new SistemaDeArchivos();
        ServicioGeoref servicioGeoref = ServicioGeoref.instancia();

        RepositorioDeOrganismosDeControl repositorioDeOrganismos = RepositorioDeOrganismosDeControl.getInstancia();
        empresas = cargadorDeDatos.cargaDeDatosMASIVA(sistemaDeArchivos.csvALista(Config.ARCHIVO_CSV), servicioGeoref);
        empresas.forEach(e -> repositorioDeOrganismos.agregar(e));

    }
}


