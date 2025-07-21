package models.persistence;


import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerSingleton {
  private static EntityManager instancia = null;
  private EntityManagerSingleton(){}

  public static EntityManager getInstance(){
    if(instancia == null || !instancia.isOpen()) {
      Map<String, String> env = System.getenv();
      Map<String, Object> configOverrides = new HashMap<String, Object>();

      String[] keys = new String[] {
              "DATABASE_URL",
              "hibernate__hbm2ddl__auto",
              "hibernate__connection__pool_size",
              "hibernate__show_sql" };

      for (String key : keys) {

        try{
          if (key.equals("DATABASE_URL")) {
            String value = env.get(key);
            URI uri = new URI(value.substring(6)); // Removing the "mysql:" prefix

            String userInfo = uri.getUserInfo();
            String[] userPass = userInfo.split(":");
            String username = userPass[0];
            String password = userPass.length > 1 ? userPass[1] : null;

            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath().substring(1);
            configOverrides.put("javax.persistence.jdbc.url", "jdbc:mysql://"+host+":"+port+"/"+path);
            configOverrides.put("javax.persistence.jdbc.user", username);
            configOverrides.put("javax.persistence.jdbc.password", password);
            configOverrides.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
          }
          String key2 = key.replace("__",".");
          if (env.containsKey(key)) {
            String value = env.get(key);
            configOverrides.put(key2, value);
          }
        } catch(Exception ex){
          System.out.println("Error configurando " + key);
        }
      }
      System.out.println("Config overrides ----------------------");
      for (String key : configOverrides.keySet()) {
        System.out.println(key + ": " + configOverrides.get(key));
      }

      String persistenceUnit = "simple-persistence-unit";

      instancia = Persistence.createEntityManagerFactory(persistenceUnit, configOverrides).createEntityManager();
    }
    return instancia;
  }

  public static void main(String[] args) {
    String databaseUrl = "mysql://root:123456@localhost:3306/tpa-grupo9";

    try {
      URI uri = new URI(databaseUrl.substring(6)); // Removing the "mysql:" prefix

      String userInfo = uri.getUserInfo();
      String[] userPass = userInfo.split(":");
      String user = userPass[0];
      String password = userPass.length > 1 ? userPass[1] : null;

      String host = uri.getHost();
      int port = uri.getPort();
      String path = uri.getPath().substring(1); // Removing the leading slash

      System.out.println("User: " + user);
      System.out.println("Password: " + password);
      System.out.println("Host: " + host);
      System.out.println("Port: " + port);
      System.out.println("Database: " + path);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
