package models.services.APIs.ServicioGradosDeConfianza.clases;

import lombok.Getter;
import lombok.Setter;
import models.domain.Personas.Comunidad;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SGDComunidad {
  private String gradoDeConfianza;
  private List<SGDUsuario> miembros;

  public SGDComunidad(Comunidad comunidad) {
    List<SGDUsuario> miembros = new ArrayList<>();
    comunidad.getMiembros().forEach(m -> miembros.add(new SGDUsuario(m)));
    this.setMiembros(miembros);
    this.setGradoDeConfianza(comunidad.getGradosDeConfianza());
  }
}
