package repositories;

import entities.News;
import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRepositoryImpl extends UnicastRemoteObject implements NewsRepository {

    private EntityManagerFactory emf;

    public NewsRepositoryImpl() throws RemoteException {
        super();
        emf = Persistence.createEntityManagerFactory("NewsPU");
    }

    @Override
    public boolean addUser(User u) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<News> getNewsByTagsOrNewsCategories(String value) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT n FROM News n LEFT JOIN n.tags t LEFT JOIN n.newsCategories c " +
                          "WHERE t LIKE :searchVal OR c LIKE :searchVal";
            return em.createQuery(jpql, News.class)
                     .setParameter("searchVal", "%" + value + "%")
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<User, Integer> getStatistics() throws RemoteException {
        EntityManager em = emf.createEntityManager();
        Map<User, Integer> result = new HashMap<>();
        try {
            String jpql = "SELECT c.user, COUNT(c.id) FROM Comment c GROUP BY c.user HAVING COUNT(c.id) >= 3";
            List<Object[]> list = em.createQuery(jpql, Object[].class).getResultList();
            
            for (Object[] obj : list) {
                User user = (User) obj[0];
                Long count = (Long) obj[1];
                result.put(user, count.intValue());
            }
            return result;
        } finally {
            em.close();
        }
    }
}