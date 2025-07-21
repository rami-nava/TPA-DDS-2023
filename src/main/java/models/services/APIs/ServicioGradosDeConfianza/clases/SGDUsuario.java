package models.services.APIs.ServicioGradosDeConfianza.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Personas.MiembroDeComunidad;

@Setter
@Getter
public class SGDUsuario {
  private String nombre;
  private String apellido;
  private Long id;
  private Long puntosDeConfianza;
  public SGDUsuario(MiembroDeComunidad miembroDeComunidad) {
    this.setId(miembroDeComunidad.getId());
    this.setNombre(miembroDeComunidad.getNombre());
    this.setApellido(miembroDeComunidad.getApellido());
    this.setPuntosDeConfianza(miembroDeComunidad.getPuntosDeConfianza());
  }
}
