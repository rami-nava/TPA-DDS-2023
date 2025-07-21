package models.persistence.Converters;

import models.domain.Notificaciones.MedioDeComunicacion;
import models.domain.Notificaciones.ViaMail;
import models.domain.Notificaciones.ViaWPP;

import javax.persistence.AttributeConverter;
import java.util.Objects;

public class MedioDeComunicacionAttributeConverter implements AttributeConverter<MedioDeComunicacion, String> {
  @Override
  public String convertToDatabaseColumn(MedioDeComunicacion medioDeComunicacion) {
    String nombreMedio = "";

    if (medioDeComunicacion == null)
      return null;

    switch (medioDeComunicacion.getClass().getName()) {
      case "models.domain.Notificaciones.ViaMail": nombreMedio = "mail"; break;
      case "models.domain.Notificaciones.ViaWPP": nombreMedio = "wpp"; break;
    }

    return nombreMedio;
  }

  @Override
  public MedioDeComunicacion convertToEntityAttribute(String s) {
    MedioDeComunicacion medio = null;

    if (s == null)
      medio = null;

    if (Objects.equals(s, "wpp"))
      medio = new ViaWPP();

    if (Objects.equals(s, "mail"))
      medio = new ViaMail();

    return medio;
  }
}
