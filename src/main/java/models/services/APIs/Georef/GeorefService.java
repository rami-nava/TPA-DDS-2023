package models.services.APIs.Georef;

import models.services.Localizacion.ListadoDeMunicipios;
import models.services.Localizacion.ListadoDeProvincias;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeorefService {
    @GET("provincias")
    Call<ListadoDeProvincias> provincias();

    @GET("municipios")
    Call<ListadoDeMunicipios> municipios(@Query("provincia") int idProvincia, @Query("campos") String campos, @Query("max") int max);

    @GET("municipios")
    Call<ListadoDeMunicipios> municipios(@Query("campos") String campos, @Query("max") int max);
}

