package models.domain.Seguridad.Reglas;

public class NoContieneSecuenciasRepetidas implements ReglaContrasenia{
    public boolean cumpleRegla(String contrasenia) {

        int largo = contrasenia.length();
        for (int i = 1; i <= Math.floor(largo / 2); i++) {
            for (int j = 0; j <= largo - 2 * i; j++) {
                String subcad1 = contrasenia.substring(j, j + i);
                String subcad2 = contrasenia.substring(j + i, 2 * i + j);
                if ((subcad1).equals(subcad2)) {
                    return false;
                }
            }
        }
        return true;
    }
}
