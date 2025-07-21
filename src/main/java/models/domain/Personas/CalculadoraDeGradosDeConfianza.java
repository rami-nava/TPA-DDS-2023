package models.domain.Personas;

import models.domain.Incidentes.Incidente;
import models.services.APIs.ServicioGradosDeConfianza.ServicioGDAPIREST;
import models.services.APIs.ServicioGradosDeConfianza.ServicioGradosDeConfianza;

import java.util.List;

public class CalculadoraDeGradosDeConfianza { //esta clase sirve para tener un único punto de acoplamiento con el servicio APIREST
    private ServicioGradosDeConfianza servicioGradosDeConfianza = ServicioGDAPIREST.instancia();
    public Long gradosDeConfianzaComunidad(Comunidad comunidad, List<Incidente> incidentes) {
        return servicioGradosDeConfianza.obtenerGradoDeConfianzaComunidad(comunidad, incidentes);
    }
    public Long gradosDeConfianzaMiembroDeComunidad(MiembroDeComunidad miembroDeComunidad, List<Incidente> incidentes) {
        return servicioGradosDeConfianza.obtenerGradoDeConfianzaMiembroDeComunidad(miembroDeComunidad, incidentes);
    }
}
