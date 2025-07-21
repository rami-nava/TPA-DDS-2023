package server.handlers;

import io.javalin.http.Context;
import models.Config.Config;


public class SessionHandler {
  private static Long tiempoVencimientoSegundos = Config.TIMEOUT;
  private static Long tiempoUbicacionSegundos = Config.TIMELOCATION;

  public static void createSession(Context context, long id, String tipoRol ){
    Long fechaDeVencimiento = System.currentTimeMillis() + (tiempoVencimientoSegundos * 1000);

    context.cookie("usuario_id",String.valueOf(id), tiempoVencimientoSegundos.intValue());
    context.cookie("tipo_rol", tipoRol, tiempoVencimientoSegundos.intValue());
    context.cookie("vencimiento", String.valueOf(fechaDeVencimiento), tiempoVencimientoSegundos.intValue());
  }
  public static void updateSession(Context context) {
    context.cookie("usuario_id", String.valueOf(context.cookie("usuario_id")), tiempoVencimientoSegundos.intValue());
    context.cookie("tipo_rol", String.valueOf(context.cookie("tipo_rol")), tiempoVencimientoSegundos.intValue());
    context.cookie("vencimiento", String.valueOf(tiempoVencimientoSegundos*1000+System.currentTimeMillis()), tiempoVencimientoSegundos.intValue());
  }

  public static boolean checkSession(Context ctx) {
    String fechaDeVencimiento = ctx.cookie("vencimiento");
    //String fechaDeVencimiento = "123";
    return (ctx.cookie("usuario_id") != null) &&
            !(Long.parseLong(fechaDeVencimiento) < System.currentTimeMillis());
  }

  public static void createLocationCookie(Context context){
    Long fechaDeVencimiento = System.currentTimeMillis() + (tiempoUbicacionSegundos * 1000);
    context.cookie("vencimiento_ubicacion",String.valueOf(fechaDeVencimiento),tiempoUbicacionSegundos.intValue());
  }
  public static boolean checkLocationCookie(Context ctx) {
    String fechaDeVencimiento = ctx.cookie("vencimiento_ubicacion");

    if (ctx.cookie("vencimiento_ubicacion") != null) {
      long vencimiento = Long.parseLong(fechaDeVencimiento);
      if (vencimiento < System.currentTimeMillis()){
        ctx.removeCookie("vencimiento_ubicacion");
        return false;
      } else {
        return true;
      }
    }
    return false;
  }


  public static String getUserID(Context ctx) {
    return ctx.cookie("usuario_id");
  }
  public static String getTipoRol(Context ctx) {
    return ctx.cookie("tipo_rol");
  }

  public static void endSession(Context ctx) {
    ctx.removeCookie("usuario_id");
    ctx.removeCookie("tipo_rol");
    ctx.removeCookie("vencimiento");
    ctx.removeCookie("vencimiento_ubicacion");
    ctx.redirect("/");
  }
}
