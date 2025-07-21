package models.services.APIs.ServicioGradosDeConfianza;

import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;

import java.util.List;

public interface ServicioGradosDeConfianza {
  Long obtenerGradoDeConfianzaComunidad(Comunidad comunidad, List<Incidente> incidentes);
  Long obtenerGradoDeConfianzaMiembroDeComunidad(MiembroDeComunidad miembroDeComunidad, List<Incidente> incidentes);
}
