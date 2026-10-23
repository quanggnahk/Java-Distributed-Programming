package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;
import java.util.function.Function;

public class GenericDAO<T, ID> {

    private final EntityManagerFactory emf;
    private final Class<T> entityClass;

    public GenericDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    private <R> R inTransaction(Function<EntityManager, R> work) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // ---------- Create ----------
    public T create(T entity) {
        return inTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    // ---------- Update ----------
    public T update(T entity) {
        return inTransaction(em -> em.merge(entity));
    }

    // ---------- Delete ----------
    public boolean delete(ID id) {
        return inTransaction(em -> {
            T found = em.find(entityClass, id);
            if (found == null) {
                return false;
            }
            em.remove(found);
            return true;
        });
    }

    // ---------- Find by Id ----------
    public T findById(ID id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    // ---------- Get All ----------
    public List<T> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
            cq.select(cq.from(entityClass));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}