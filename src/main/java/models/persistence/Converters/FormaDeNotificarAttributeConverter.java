package models.persistence.Converters;

import models.domain.Notificaciones.CuandoSuceden;
import models.domain.Notificaciones.FormaDeNotificar;
import models.domain.Notificaciones.SinApuros;

import javax.persistence.AttributeConverter;
import java.util.Objects;

public class FormaDeNotificarAttributeConverter implements AttributeConverter<FormaDeNotificar, String> {
  @Override
  public String convertToDatabaseColumn(FormaDeNotificar formaDeNotificar) {
    String nombreForma = "";

    if (formaDeNotificar == null)
      return null;

    switch (formaDeNotificar.getClass().getName()) {
      case "models.domain.Notificaciones.CuandoSuceden": nombreForma = "cuandoSuceden"; break;
      case "models.domain.Notificaciones.SinApuros": nombreForma = "sinApuros"; break;
    }

    return nombreForma;
  }

  @Override
  public FormaDeNotificar convertToEntityAttribute(String s) {
    FormaDeNotificar forma = null;

    if (s == null)
      forma = null;

    if (Objects.equals(s, "cuandoSuceden"))
      forma = new CuandoSuceden();

    if (Objects.equals(s, "sinApuros"))
      forma = new SinApuros();

    return forma;
  }
}