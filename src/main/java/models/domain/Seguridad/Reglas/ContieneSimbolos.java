package models.domain.Seguridad.Reglas;

import java.util.List;

public class ContieneSimbolos implements ReglaContrasenia{
    private List<Character> simbolos;

    public ContieneSimbolos(List<Character> simbolos) {
        this.simbolos = simbolos;
    }

    public boolean cumpleRegla(String contrasenia){
        return simbolos.stream().anyMatch(s->contrasenia.contains(s.toString()));
    }
}
