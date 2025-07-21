package models.persistence.Repositorios;

import models.persistence.EntityManagerSingleton;
import models.services.Localizacion.Municipio;

import javax.persistence.EntityManager;


public class RepositorioDeMunicipios extends RepositorioGenerico<Municipio> {
  private static RepositorioDeMunicipios instancia = null;
  private EntityManager entityManager = EntityManagerSingleton.getInstance();
  private RepositorioDeMunicipios() {
    super(Municipio.class);
  }

  public static  RepositorioDeMunicipios getInstancia() {
    if (instancia == null) {
      instancia = new RepositorioDeMunicipios();
    }
    return instancia;
  }

  @Override
  public Municipio buscar(long id) {
    entityManager = EntityManagerSingleton.getInstance();
    return entityManager.find(Municipio.class, (int)id);
  }
}
