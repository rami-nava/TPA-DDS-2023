package models.domain.Rankings;

import models.Config.Config;
import models.domain.Entidades.Entidad;
import models.domain.Incidentes.Incidente;
import models.services.Archivos.SistemaDeArchivos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntidadesConMayorCantidadDeIncidentes extends Tierlist{
    /*Entidades con mayor cantidad de incidentes reportados en la semana.
    Una vez que un incidente sobre una prestación es reportado por algún usuario,
    independientemente de la comunidad de la que forma parte, no se consideran, para el presente ranking, ningún
    incidente que se genere sobre dicha prestación en un plazo de 24 horas siempre y cuando el mismo continúe abierto. */
    /*Se considera el período semanal desde el lunes a las 0.00 h. hasta el domingo 23.59 h.*/


    @Override
    protected int[] obtenerValoresPorEntidad(List<Entidad> entidades, List<Incidente> incidentes) {
        int[] contadorAux = new int[entidades.size()];

        /*
        Ordenar los incidentes por horario y filtrar por tipo de incidente y recorrer
        uno por uno, contar a partir del horario de apertura
        que hayan pasado 24 horas, o si fue cerrado, avanzar hasta el proximo incidente luego de dicho horario
        y seguir recorriendo
         */

        for(Incidente incidente : incidentes) {
            Optional<Entidad> entidadConIncidente = entidades.stream().filter(entidad -> entidad.getEstablecimientos().contains(incidente.getEstablecimiento())).findFirst();
            int cantidadDiasAbierto = incidente.diasAbierto();
            contadorAux[entidades.indexOf(entidadConIncidente.get())] += cantidadDiasAbierto;
        }
        return contadorAux;
    }

    @Override
    protected void generarRanking(List<Entidad> entidadesOrdenadas,List<Entidad> entidades,int[] contadorAux) {
        List<String[]> listaDeStrings = new ArrayList<>();
        entidadesOrdenadas.forEach(entidad ->
            listaDeStrings.add(new String[]
                {entidad.getNombre(),entidad.getTipoEntidad().toString(),Integer.toString(contadorAux[entidades.indexOf(entidad)])}));
        //el contador no esta ordenado, sigue con los indices de la lista inicial

        SistemaDeArchivos sistemaDeArchivos = new SistemaDeArchivos();
        String[] encabezado = {"Nombre Entidad","Tipo Entidad","Cantidad de Incidentes reportados en la semana"};
        sistemaDeArchivos.escribirRanking(Config.RANKING_2, encabezado, listaDeStrings);
    }
}

