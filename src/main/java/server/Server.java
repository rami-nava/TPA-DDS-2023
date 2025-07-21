package server;

import com.github.jknack.handlebars.Template;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;
import io.javalin.rendering.JavalinRenderer;
import com.github.jknack.handlebars.Handlebars;

import server.handlers.AppHandlers;
import server.middlewares.AutenticacionMiddleware;

import java.io.IOException;

import java.util.function.Consumer;

public class Server {
  private static Javalin app = null;

  public static Javalin app() {
    if(app == null)
      throw new RuntimeException("App no inicializada");
    return app;
  }

  public static void init() {
    String strport = System.getenv("PORT");
    if (strport == null){
      strport = "8008";
    }
    if(app == null) {
      Integer port = Integer.parseInt(System.getProperty("port", strport));
      app = Javalin.create(config()).start(port);
      initTemplateEngine();
      AppHandlers.applyHandlers(app);
      Router.init();
    }
  }

  private static Consumer<JavalinConfig> config() {
    return config -> {
      config.staticFiles.add(staticFiles -> {
        staticFiles.hostedPath = "/";
        staticFiles.directory = "/public";
      });
      AutenticacionMiddleware.apply(config);
    };
  }


  private static void initTemplateEngine() {
    JavalinRenderer.register(
        (path, model, context) -> { // Función que renderiza el template
          Handlebars handlebars = new Handlebars();
          Template template = null;
          try {
            template = handlebars.compile(
                "templates/" + path.replace(".hbs",""));
            return template.apply(model);
          } catch (IOException e) {
            e.printStackTrace();
            context.status(HttpStatus.NOT_FOUND);
            return "No se encuentra la página indicada...";
          }
        }, ".hbs" // Extensión del archivo de template
    );
  }

}