package models.persistence.Repositorios;

import models.domain.Seguridad.RegistroDeUsuarioException;
import models.domain.Usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeUsuarios extends RepositorioGenerico<Usuario> {
    private List<Usuario> usuarios;
    private static RepositorioDeUsuarios instancia = null;

    private RepositorioDeUsuarios() {
        super(Usuario.class);
        usuarios = new ArrayList<>();
    }

    public static  RepositorioDeUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioDeUsuarios();
        }
        return instancia;
    }
    public void agregarUsername(Usuario usuario) throws RegistroDeUsuarioException {
        if (usuarios.stream().anyMatch(u -> (usuario.getUsername() == u.getUsername()))) {
            throw new RegistroDeUsuarioException("El nombre de usuario ya ha sido utilizado");
        }
        else {
            usuarios.add(usuario);
            this.agregar(usuario);
        }
    }
}
