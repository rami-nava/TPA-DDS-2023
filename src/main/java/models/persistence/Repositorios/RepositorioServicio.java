package models.persistence.Repositorios;

import models.domain.Servicios.Servicio;
import models.persistence.EntityManagerSingleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;


public class RepositorioServicio extends RepositorioGenerico<Servicio> {
  private static RepositorioServicio instancia = null;

  private RepositorioServicio() {
    super(Servicio.class);
  }

  public static RepositorioServicio getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioServicio();
    }
    return instancia;
  }

  public long establecimientoDeServicio(Long servicioId){
    EntityManager entityManager = EntityManagerSingleton.getInstance();
    String sql = "SELECT establecimiento_id FROM servicio WHERE id = :servicioId";
    Query query = entityManager.createNativeQuery(sql);
    query.setParameter("servicioId", servicioId);
    BigInteger result = (BigInteger) query.getSingleResult(); //Si lo tratabamos como long de una, rompia en hibernate
    return result.longValue();
  }
}