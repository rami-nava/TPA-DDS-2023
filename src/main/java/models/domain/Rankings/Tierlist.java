package models.domain.Rankings;

import models.domain.Entidades.Entidad;
import models.domain.Incidentes.Incidente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Tierlist {
    protected List<Entidad> ordenarEntidades(List<Entidad> entidades, int[] auxiliar){
        List<Entidad> entidadesOrdenadas = new ArrayList<>(entidades);
        Collections.sort(entidadesOrdenadas, new Comparator<Entidad>() {
            @Override
            public int compare(Entidad entidad1, Entidad entidad2) {
                int index1 = entidades.indexOf(entidad1);
                int index2 = entidades.indexOf(entidad2);
                return Integer.compare(auxiliar[index2], auxiliar[index1]);
            }
        });
        return entidadesOrdenadas;
    }

    protected abstract int[] obtenerValoresPorEntidad(List<Entidad> entidades, List<Incidente> incidentes);
    protected abstract void generarRanking(List<Entidad> entidadesOrdenadas,List<Entidad> entidades,int[] contadorAux);
    public void armarRanking(List<Entidad> entidades, List<Incidente> incidentes){
        int[] contadorAux = new int[entidades.size()];
        List<Entidad> entidadesOrdenadas = new ArrayList<>();

        contadorAux = obtenerValoresPorEntidad(entidades,incidentes);
        entidadesOrdenadas = ordenarEntidades(entidades,contadorAux);
        generarRanking(entidadesOrdenadas,entidades,contadorAux);
    }
}
