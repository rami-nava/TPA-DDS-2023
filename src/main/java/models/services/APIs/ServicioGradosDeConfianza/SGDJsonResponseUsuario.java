package models.services.APIs.ServicioGradosDeConfianza;

import lombok.Getter;
import lombok.Setter;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDUsuario;

@Setter
@Getter
public class SGDJsonResponseUsuario {
    private SGDUsuario usuario;
    private Long nuevoPuntaje;
    private Long gradoDeConfianzaActual;
}
