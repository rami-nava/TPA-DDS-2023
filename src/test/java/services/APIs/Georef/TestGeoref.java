package services.APIs.Georef;

import models.services.APIs.Georef.ServicioGeoref;
import models.services.Localizacion.ListadoDeMunicipios;
import models.services.Localizacion.ListadoDeProvincias;
import models.services.Localizacion.Municipio;
import models.services.Localizacion.Provincia;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Optional;

public class TestGeoref {
    static ServicioGeoref servicioGeoref;
    @BeforeAll
    public static void init(){
        servicioGeoref = ServicioGeoref.instancia();
    }
    @Test
    public void testProvincias(){
        ListadoDeProvincias listadoDeProvinciasArgentinas = servicioGeoref.listadoDeProvincias();

        for(Provincia unaProvincia: listadoDeProvinciasArgentinas.provincias){
            System.out.println(unaProvincia.nombre +" " + unaProvincia.id);
        }

        Optional<Provincia> posibleProvincia = listadoDeProvinciasArgentinas.provinciaDeId(6);

        Assertions.assertEquals((posibleProvincia.get().nombre), "Buenos Aires");
    }
    @Test
    public void testMunicipios(){
        ListadoDeMunicipios municipiosDeLaProvincia = servicioGeoref.listadoDeMunicipiosDeProvincia(servicioGeoref.listadoDeProvincias().provinciaDeId(6).get());
        for(Municipio unMunicipio: municipiosDeLaProvincia.municipios){
            System.out.println(unMunicipio.nombre + " " + unMunicipio.id);
        }
    }
    @Test
    public void testProvincia(){
        Provincia provincia = servicioGeoref.obtenerProvincia("Buenos Aires");
        Assertions.assertEquals(6,provincia.getId());
    }
    @Test
    public void testMunicipio(){
        Municipio municipio = servicioGeoref.obtenerMunicipio("General Alvarado");
        Assertions.assertEquals(60280,municipio.getId());
    }
}
