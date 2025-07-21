package models.services.APIs.ServicioRankingProblematicas;

import models.Config.Config;
import models.domain.Entidades.Entidad;
import models.domain.Incidentes.Incidente;
import models.domain.Personas.Comunidad;
import models.services.APIs.Georef.NoSePudoConectarConAPI;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPComunidad;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPEntidad;
import models.services.APIs.ServicioRankingProblematicas.clases.SRPIncidente;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicioRankingProblematicasAPIREST implements ServicioRankingProblematicas {
    private static ServicioRankingProblematicasAPIREST instancia = null;
    private static final String urlApi = Config.URL_APIS3;
    private Retrofit retrofit;
    private ServicioRankingProblematicasMensajes servicioRankingProblematicasMensajes;
    private ServicioRankingProblematicasAPIREST() {
        this.retrofit = new Retrofit.Builder()
            .baseUrl(urlApi)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        this.servicioRankingProblematicasMensajes = this.retrofit.create(ServicioRankingProblematicasMensajes.class);
    }

    public static ServicioRankingProblematicasAPIREST instancia(){
        if(instancia== null){
            instancia = new ServicioRankingProblematicasAPIREST();
        }
        return instancia;
    }
    @Override
    public List<Entidad>  obtenerRanking(List<Entidad> entidades, List<Incidente> incidentes, List<Comunidad> comunidades, Long CNF) {
        SRPJsonRequest request = new SRPJsonRequest();

        List<SRPEntidad> miniEntidades = new ArrayList<>();
        entidades.forEach(e -> miniEntidades.add(new SRPEntidad(e)));

        List<SRPIncidente> miniIncidentes = new ArrayList<>();
        incidentes.forEach(i -> miniIncidentes.add(new SRPIncidente(i)));

        List<SRPComunidad> miniComunidades = new ArrayList<>();
        comunidades.forEach(c -> miniComunidades.add(new SRPComunidad(c, incidentes)));

        request.setEntidades(miniEntidades);
        request.setIncidentes(miniIncidentes);
        request.setComunidades(miniComunidades);
        request.setCNF(CNF);

        try {
            Call<SRPJsonResponse> requestRanking = servicioRankingProblematicasMensajes.enviarDatosRanking(request);
            Response<SRPJsonResponse> responseRanking = requestRanking.execute();
            SRPJsonResponse response = responseRanking.body();
            List<Entidad> entidadesDevueltas = new ArrayList<>();
            response.getEntidades().forEach(l -> entidadesDevueltas.add(entidades.stream().filter(e -> e.getId().equals(l)).findFirst().get()));
            return entidadesDevueltas;
        }
        catch (IOException e)
        {
            throw new NoSePudoConectarConAPI("No se pudo conectar con la API Servicio3");
        }
    }
}