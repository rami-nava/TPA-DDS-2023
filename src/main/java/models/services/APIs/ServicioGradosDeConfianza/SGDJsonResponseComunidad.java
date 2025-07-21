package models.services.APIs.ServicioGradosDeConfianza;

import lombok.Getter;
import lombok.Setter;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDComunidad;

@Setter
@Getter
public class SGDJsonResponseComunidad {
    private SGDComunidad comunidad;
    private Long nuevoPuntaje;
    private Long gradoDeConfianzaActual;
}
