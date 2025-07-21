package models.domain.Usuario;

import models.domain.Seguridad.RegistroDeUsuarioException;
import models.domain.Seguridad.ValidadorDeContrasenias;
import models.persistence.Persistente;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="usuario")
public class Usuario extends Persistente {
    @Column(name = "username")
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    @Column(name = "password")
    private String contrasenia;
    @Transient
    private ValidadorDeContrasenias validador = new ValidadorDeContrasenias();
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    private Rol rol;

  public Usuario() throws RegistroDeUsuarioException {
      this.validador = new ValidadorDeContrasenias();
    }
    public void cambiarContrasenia(String nuevaContrasenia) throws RegistroDeUsuarioException {
      if(nuevaContrasenia != contrasenia) {
          validador.validarContrasenia(nuevaContrasenia);
          this.contrasenia = nuevaContrasenia;
      }
    }
}

