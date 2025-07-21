package models.services.APIs.Georef;

import models.services.Localizacion.Municipio;
import models.services.Localizacion.Provincia;

public interface AdapterServicioGeo {
  public Municipio obtenerMunicipio(String nombre);
  public Provincia obtenerProvincia(String nombre);
}
