package models.services.APIs.Georef;

import models.Config.Config;
import models.services.Localizacion.ListadoDeMunicipios;
import models.services.Localizacion.ListadoDeProvincias;
import models.services.Localizacion.Municipio;
import models.services.Localizacion.Provincia;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ServicioGeoref implements AdapterServicioGeo{
    private static ServicioGeoref instancia = null;
    private static int maximaCantidadRegistrosDefault = 2500;
    private static final String urlApi = Config.URL_API;
    private Retrofit retrofit;
    private GeorefService georefService;


    private ServicioGeoref() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.georefService = this.retrofit.create(GeorefService.class);
    }

    public static ServicioGeoref instancia(){
        if(instancia== null){
            instancia = new ServicioGeoref();
        }
        return instancia;
    }

    public ListadoDeProvincias listadoDeProvincias(){
        try {
            Call<ListadoDeProvincias> requestProvinciasArgentinas = georefService.provincias();
            Response<ListadoDeProvincias> responseProvinciasArgentinas = requestProvinciasArgentinas.execute();
            return responseProvinciasArgentinas.body();
        }
        catch (IOException e)
        {
            throw new NoSePudoConectarConAPI("No se pudo conectar con la API Georef");
        }
    }

    public ListadoDeMunicipios listadoDeMunicipios(){
        try {
            Call<ListadoDeMunicipios> requestMunicipiosArgentinos = georefService.municipios("id, nombre, provincia", maximaCantidadRegistrosDefault);
            Response<ListadoDeMunicipios> responseMunicipiosArgentinos = requestMunicipiosArgentinos.execute();
            return responseMunicipiosArgentinos.body();
        }
        catch (IOException e)
        {
            throw new NoSePudoConectarConAPI("No se pudo conectar con la API Georef");
        }
    }

    public ListadoDeMunicipios listadoDeMunicipiosDeProvincia(Provincia provincia){
        try {
            Call<ListadoDeMunicipios> requestListadoDeMunicipios = georefService.municipios(provincia.id, "id, nombre, provincia", maximaCantidadRegistrosDefault);
            Response<ListadoDeMunicipios> responseListadoDeMunicipios = requestListadoDeMunicipios.execute();
            return responseListadoDeMunicipios.body();
        }
        catch (IOException e)
        {
            throw new NoSePudoConectarConAPI("No se pudo conectar con la API Georef");
        }
    }

    @Override
    public Municipio obtenerMunicipio(String nombre){
        return this.listadoDeMunicipios().municipios.stream().filter(municipio -> municipio.getNombre().equals(nombre)).toList().get(0);
    }

    @Override
    public Provincia obtenerProvincia(String nombre){
        return this.listadoDeProvincias().provincias.stream().filter(provincia -> provincia.getNombre().equals(nombre)).toList().get(0);
    }
}
