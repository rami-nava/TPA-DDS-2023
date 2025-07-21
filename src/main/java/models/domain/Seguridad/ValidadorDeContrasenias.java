package models.domain.Seguridad;

import models.Config.Config;
import models.domain.Seguridad.Reglas.*;

import java.util.ArrayList;
import java.util.List;

public class ValidadorDeContrasenias {
  private List<ReglaContrasenia> reglas;
  private List<Character> simbolos;
  public ValidadorDeContrasenias(){
    reglas = new ArrayList<>();
    simbolos = new ArrayList<>();

    simbolos.add('-');
    simbolos.add('_');
    simbolos.add('!');
    simbolos.add('@');
    simbolos.add('#');
    simbolos.add('$');
    simbolos.add('%');
    simbolos.add('^');
    simbolos.add('&');
    simbolos.add('*');
    simbolos.add('/');
    simbolos.add('=');
    simbolos.add('+');

    reglas.add(new AlMenosUnaMayuscula());
    reglas.add(new ContieneSimbolos(simbolos));
    reglas.add(new NoContieneSecuenciasRepetidas());
    reglas.add(new LongitudMinima());
    reglas.add(new NoEstaEnArchivo(Config.ARCHIVO_CONTRASENIAS_COMUNES_RUTA));
    reglas.add(new NoEstaEnArchivo(Config.ARCHIVO_DICCIONARIO_RUTA));
  }
  public void validarContrasenia(String contrasenia){
    if (!verificaReglas(contrasenia)) {
      throw new RegistroDeUsuarioException("Contraseña inválida");
    };
  }

  public boolean verificaReglas(String contrasenia) {
    return reglas.stream().allMatch(regla -> regla.cumpleRegla(contrasenia));
  }

}
