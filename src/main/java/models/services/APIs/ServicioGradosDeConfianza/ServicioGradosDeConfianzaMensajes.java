package models.services.APIs.ServicioGradosDeConfianza;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServicioGradosDeConfianzaMensajes {
    @POST("/gradoDeConfianza/comunidad")
    Call<String> enviarDatosGradoDeConfianzaComunidad(@Body String jsonRequest);
    @POST("/gradoDeConfianza/usuario")
    Call<String> enviarDatosGradoDeConfianzaMiembroDeComunidad(@Body String jsonRequest);
}