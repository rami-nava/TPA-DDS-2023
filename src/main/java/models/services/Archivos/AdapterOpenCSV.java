package models.services.Archivos;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AdapterOpenCSV implements AdapterLectorCSV {
    @Override
    public List<String[]> leer(String ruta) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(ruta)).build();
            reader.skip(1); //no lee el encabezado
            List<String[]> lista = reader.readAll();

            return lista;
        }
        catch (IOException | CsvException e) {
            throw new NoSePudoLeerArchivoCSV("No se pudo leer el archivo CSV");
        }
    }

    @Override
    public void escribir(String ruta, String[] encabezado ,List<String[]> lineas) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(ruta));

            writer.writeNext(encabezado);

            lineas.forEach(linea -> writer.writeNext(linea));

            writer.close();
        }
        catch (IOException e) {
            throw new NoSePudoLeerArchivoCSV("No se pudo escribir sobre el archivo CSV");
        }
    }
}
