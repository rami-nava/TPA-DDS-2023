package models.services.APIs.ServicioGradosDeConfianza;

import lombok.Getter;
import lombok.Setter;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDComunidad;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDIncidente;

import java.util.List;

@Setter
@Getter
public class SGDJsonRequestComunidad {
  private SGDComunidad comunidad;
  private List<SGDIncidente> incidentes;
}
