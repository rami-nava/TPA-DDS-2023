package Seguridad;

import models.domain.Seguridad.RegistroDeUsuarioException;
import models.domain.Seguridad.ValidadorDeContrasenias;
import models.domain.Usuario.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDeValidacionDeContrasenias {
  ValidadorDeContrasenias validador = new ValidadorDeContrasenias();

  public void testearContrasenia(String contrasenia) {
    Assertions.assertThrows(RegistroDeUsuarioException.class, () -> validador.validarContrasenia(contrasenia));
  }

  @Test
  public void inicioDeSesionFallido() {
    Assertions.assertThrows(RegistroDeUsuarioException.class, () -> {
      Usuario usuario = new Usuario();
      usuario.setUsername("juan");
      usuario.cambiarContrasenia("LaCasaEnElLago");
    });
  }

  @Test
  public void inicioDeSesionExitoso() {
    Assertions.assertDoesNotThrow(() -> {
      Usuario usuario = new Usuario();
      usuario.setUsername("juan");
      usuario.cambiarContrasenia("LaCasaEnElLag@");
    });
  }
  @Test
  public void contraseniaFacil() {
    testearContrasenia("1234");
  }

  @Test
  public void contraseniaSinMayuscula() {
   testearContrasenia("al@ha12345");
  }

  @Test
  public void contraseniaSinSimbolos() {
    testearContrasenia("aLoHa1212");
  }

  @Test
  public void contraseniaDeDiccionario() {
    testearContrasenia("elefante");
  }

  @Test
  public void contraseniaCorta() {
   testearContrasenia("aL@Ha12");
  }

  @Test
  public void contraseniaConSecuenciaRepetida() {
    testearContrasenia("a@Baaaaaaaaaaaaa");
  }

  @Test
  public void contraseniaValida() {
    Assertions.assertDoesNotThrow(() -> validador.validarContrasenia("CasaEnElLag@"));
  }
}
