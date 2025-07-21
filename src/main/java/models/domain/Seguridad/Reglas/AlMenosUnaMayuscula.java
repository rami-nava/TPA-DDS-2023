package models.domain.Seguridad.Reglas;

public class AlMenosUnaMayuscula implements ReglaContrasenia{
    public boolean cumpleRegla(String contrasenia) {
        return !contrasenia.equals(contrasenia.toLowerCase());
    }
}
