package models.services.Archivos;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SistemaDeArchivos {
  public boolean estaEnArchivo(String texto, String ruta) throws IOException {
    String linea = null;
    FileReader fr = null;
    BufferedReader br = null;

    boolean encontrado = false;


    File archivo = null;
    archivo = new File(ruta);
    fr = new FileReader(archivo);
    br = new BufferedReader(fr);
    while ((linea = br.readLine()) != null) {
      if (texto.equals(linea)) encontrado = true;
    }

    if (null != fr) {
      fr.close();
    }

    return encontrado;
  }

  public List<String[]> csvALista(String ruta){
    AdapterLectorCSV adapter = new AdapterOpenCSV(); //punto de acoplamiento con librería externa
    return adapter.leer(ruta);
  }

  public void escribirRanking(String ruta, String[] encabezado, List<String[]> lineas){
    AdapterLectorCSV adapter = new AdapterOpenCSV();
    adapter.escribir(ruta,encabezado,lineas);
  }
}

