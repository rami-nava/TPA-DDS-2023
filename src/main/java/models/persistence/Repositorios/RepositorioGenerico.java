package models.persistence.Repositorios;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.persistence.EntityManagerSingleton;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class RepositorioGenerico<T> implements WithSimplePersistenceUnit {
    @PersistenceContext
    private Class<T> entityClass;
    private EntityManager entityManager;
    public RepositorioGenerico(Class<T> entityClass) { this.entityClass = entityClass; }
    public void agregar(T entity) {
        entityManager = EntityManagerSingleton.getInstance();
        entityManager.persist(entity);
    }
    public void eliminar(T entity) {
        entityManager = EntityManagerSingleton.getInstance();
        entityManager.remove(entity);
    }

    public T buscar(long id) {
        entityManager = EntityManagerSingleton.getInstance();
        return entityManager.find(entityClass, id);
    }

    public List<T> buscarTodos() {
        entityManager = EntityManagerSingleton.getInstance();
        return entityManager.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList(); }
}