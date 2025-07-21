package models.services.APIs.ServicioGradosDeConfianza;

import lombok.Getter;
import lombok.Setter;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDIncidente;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDUsuario;

import java.util.List;

@Setter
@Getter
public class SGDJsonRequestUsuario {
  private SGDUsuario usuario;
  private List<SGDIncidente> incidentes;
}
