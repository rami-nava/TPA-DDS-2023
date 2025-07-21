package models.services.APIs.ServicioRankingProblematicas;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServicioRankingProblematicasMensajes {
    @POST("/ranking")
    Call<SRPJsonResponse> enviarDatosRanking(@Body SRPJsonRequest jsonRequest);
}