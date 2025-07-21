package models.services.Archivos;


import models.domain.Entidades.*;
import models.domain.Entidades.Posicion;
import models.domain.Servicios.Banio;
import models.domain.Servicios.Elevacion;
import models.domain.Servicios.Servicio;
import models.persistence.Repositorios.RepositorioPosicion;
import models.services.APIs.Georef.AdapterServicioGeo;
import models.persistence.Repositorios.RepositorioDeMunicipios;

import java.util.*;

public class CargadorDeDatos {
  public List<OrganismoDeControl> cargaDeDatosMASIVA(List<String[]> listaCSV, AdapterServicioGeo servicioGeo){
    Map<String, OrganismoDeControl> organismosMap = new HashMap<>();
    RepositorioDeMunicipios repositorioDeMunicipios = RepositorioDeMunicipios.getInstancia();

    for (String[] elemento : listaCSV) {
      String organismoNombre = elemento[0];
      String prestadoraNombre = elemento[1];
      String entidadNombre = elemento[2];
      String entidadTipo = elemento[3];
      String establecimientoNombre = elemento[4];
      String establecimientoLocalizacion = elemento[5];
      String establecimientoTipo = elemento[6];
      String servicioNombre = elemento[7];
      String servicioTipo = elemento[8];
      String establecimientoLatitud = elemento[9];
      String establecimientoLongitud = elemento[10];


      RepositorioPosicion repositorioPosicion = RepositorioPosicion.getInstancia();
      List<Posicion> posicionesRegistradas = repositorioPosicion.buscarTodos();

      Posicion posiblePosicion = new Posicion();

      if(!posicionesRegistradas.stream().filter(posicion ->
            posicion.getLatitud() == Double.parseDouble(establecimientoLatitud)
          && posicion.getLongitud() == Double.parseDouble(establecimientoLongitud)).toList().isEmpty())
      {
        Posicion posicionRepetida = posicionesRegistradas.stream().filter(posicion ->
            posicion.getLatitud() == Double.parseDouble(establecimientoLatitud)
                && posicion.getLongitud() == Double.parseDouble(establecimientoLongitud)).toList().get(0);

        posiblePosicion = repositorioPosicion.buscar(posicionRepetida.getId());
      } else {
        posiblePosicion.setPosicion(establecimientoLatitud + "," + establecimientoLongitud);
        repositorioPosicion.agregar(posiblePosicion);
      }

      EntidadPrestadora posiblePrestadora = new EntidadPrestadora();
      posiblePrestadora.setNombre(prestadoraNombre);

      Entidad posibleEntidad = new Entidad();
      posibleEntidad.setNombre(entidadNombre);
      posibleEntidad.setTipoEntidad(TipoEntidad.valueOf(entidadTipo));

      Establecimiento posibleEstablecimiento = new Establecimiento();
      posibleEstablecimiento.setTipoEstablecimiento(TipoEstablecimiento.valueOf(establecimientoTipo));
      posibleEstablecimiento.setLocalizacion(repositorioDeMunicipios.buscar(servicioGeo.obtenerMunicipio(establecimientoLocalizacion).getId()));
      posibleEstablecimiento.setNombre(establecimientoNombre);
      posibleEstablecimiento.setPosicion(posiblePosicion);

      OrganismoDeControl organismo = organismosMap.getOrDefault(organismoNombre, new OrganismoDeControl());
      organismo.setNombre(organismoNombre);

      EntidadPrestadora prestadora = obtenerPrestadora(organismo.getEntidadesPrestadoras(), posiblePrestadora);
      Entidad entidad = obtenerEntidad(prestadora.getEntidades(), posibleEntidad);
      Establecimiento establecimiento = obtenerEstablecimiento(entidad.getEstablecimientos(), posibleEstablecimiento);

      if (servicioNombre.equals("Banio")) {
        Servicio banio = new Banio();
        banio.setTipo(servicioTipo);
        establecimiento.agregarServicio(banio);
      } else {
        Servicio elevador = new Elevacion();
        elevador.setTipo(servicioTipo);
        establecimiento.agregarServicio(elevador);
      }

      entidad.agregarEstablecimiento(establecimiento);
      prestadora.agregarEntidad(entidad);
      organismo.agregarEntidadPrestadora(prestadora);
      organismosMap.put(organismoNombre, organismo);
    }

    return new ArrayList<>(organismosMap.values());
  }

  private EntidadPrestadora obtenerPrestadora(List<EntidadPrestadora> prestadoras, EntidadPrestadora posiblePrestadora) {
    //Devuelve una ya existente o la crea
    Optional<EntidadPrestadora> entidadPrestadora = prestadoras.stream().filter(prestadora -> prestadora.igualito(posiblePrestadora)).toList().stream().findFirst();
    return entidadPrestadora.orElseGet(() -> posiblePrestadora);
  }

  private Entidad obtenerEntidad(List<Entidad> entidades, Entidad posibleEntidad) {
    //Devuelve una ya existente o la crea
    Optional<Entidad> entidad = entidades.stream().filter(entidad1 -> entidad1.igualito(posibleEntidad)).toList().stream().findFirst();
    return entidad.orElseGet(() -> posibleEntidad);
  }

  private Establecimiento obtenerEstablecimiento(List<Establecimiento> establecimientos, Establecimiento posibleEstablecimiento) {
    Optional<Establecimiento> establecimiento = establecimientos.stream().filter(establecimiento1 -> establecimiento1.igualito(posibleEstablecimiento)).toList().stream().findFirst();
    return establecimiento.orElseGet(() -> posibleEstablecimiento);
  }
}
