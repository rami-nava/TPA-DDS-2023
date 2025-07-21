package models.services.APIs.ServicioGradosDeConfianza;

import com.google.gson.Gson;
import models.Config.Config;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;
import models.domain.Personas.MiembroDeComunidad;
import models.services.APIs.Georef.NoSePudoConectarConAPI;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDComunidad;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDIncidente;
import models.services.APIs.ServicioGradosDeConfianza.clases.SGDUsuario;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicioGDAPIREST implements ServicioGradosDeConfianza {
  private static ServicioGDAPIREST instancia = null;
  private static final String urlApi = Config.URL_APIS2;
  private Retrofit retrofit;
  private ServicioGradosDeConfianzaMensajes servicioGradosDeConfianzaMensajes;
  private ServicioGDAPIREST() {
    this.retrofit = new Retrofit.Builder()
        .baseUrl(urlApi)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    this.servicioGradosDeConfianzaMensajes = this.retrofit.create(ServicioGradosDeConfianzaMensajes.class);
  }

  public static ServicioGDAPIREST instancia(){
    if(instancia== null){
      instancia = new ServicioGDAPIREST();
    }
    return instancia;
  }
  @Override
  public Long obtenerGradoDeConfianzaComunidad(Comunidad comunidad, List<Incidente> incidentes) {
    SGDJsonRequestComunidad request = new SGDJsonRequestComunidad();

    request.setComunidad(new SGDComunidad(comunidad));

    List<SGDIncidente> incidentesDeComunidad = new ArrayList<>();

    comunidad.getIncidentesDeComunidad(incidentes).forEach(i -> incidentesDeComunidad.add(new SGDIncidente(i)));

    request.setIncidentes(incidentesDeComunidad);

    Gson gson = new Gson();

    try {
      Call<String> requestEnviarDatosGradoDeConfianzaComunidad = servicioGradosDeConfianzaMensajes.enviarDatosGradoDeConfianzaComunidad(gson.toJson(request));
      Response<String> response = requestEnviarDatosGradoDeConfianzaComunidad.execute();
      SGDJsonResponseComunidad jsonResponse = gson.fromJson(response.body(), SGDJsonResponseComunidad.class);
      comunidad.setGradosDeConfianza(jsonResponse.getComunidad().getGradoDeConfianza());
      return jsonResponse.getNuevoPuntaje();
    }
    catch (IOException e)
    {
        throw new NoSePudoConectarConAPI("No se pudo conectar con la API Servicio2");
    }

  }

  @Override
  public Long obtenerGradoDeConfianzaMiembroDeComunidad(MiembroDeComunidad miembroDeComunidad, List<Incidente> incidentes) {
    SGDJsonRequestUsuario request = new SGDJsonRequestUsuario();

    request.setUsuario(new SGDUsuario(miembroDeComunidad));

    List<SGDIncidente> incidentesRequest = new ArrayList<>();

    incidentes.forEach(i -> incidentesRequest.add(new SGDIncidente(i)));

    request.setIncidentes(incidentesRequest);

    Gson gson = new Gson();

    try {
      Call<String> requestEnviarDatosGradoDeConfianzaMiembroDeComunidad = servicioGradosDeConfianzaMensajes.enviarDatosGradoDeConfianzaMiembroDeComunidad(gson.toJson(request));
      Response<String> response = requestEnviarDatosGradoDeConfianzaMiembroDeComunidad.execute();
      SGDJsonResponseUsuario jsonResponse = gson.fromJson(response.body(), SGDJsonResponseUsuario.class);
      miembroDeComunidad.setPuntosDeConfianza(jsonResponse.getNuevoPuntaje());
      return jsonResponse.getNuevoPuntaje();
    }
    catch (IOException e)
    {
      throw new NoSePudoConectarConAPI("No se pudo conectar con la API Servicio2");
    }
  }
}
