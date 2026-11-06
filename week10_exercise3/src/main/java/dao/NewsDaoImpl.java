package dao;

import entity.News;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class NewsDaoImpl implements NewsDao {
    private final EntityManagerFactory emf;

    public NewsDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean addUser(User u) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.err.println("[Server DAO Log] Lỗi addUser:");
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public List<News> getNewsByTagsOrNewsCategories(String value) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT n FROM News n " +
                          "LEFT JOIN n.tags t " +
                          "LEFT JOIN n.newsCategories c " +
                          "WHERE LOWER(t) LIKE LOWER(:val) OR LOWER(c) LIKE LOWER(:val)";
            
            List<News> newsList = em.createQuery(jpql, News.class)
                     .setParameter("val", "%" + value.toLowerCase() + "%")
                     .getResultList();

            // Chuyển đổi PersistentSet của Hibernate thành HashSet tiêu chuẩn của Java
            for (News n : newsList) {
                n.setTags(new HashSet<>(n.getTags()));
                n.setNewsCategories(new HashSet<>(n.getNewsCategories()));
                n.setVotes(new HashSet<>(n.getVotes()));
            }
            return newsList;
        } finally {
            em.close();
        }
    }

    @Override
    public Map<User, Integer> getStatistics() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c.user, COUNT(c.news.id) " +
                          "FROM Comment c " +
                          "GROUP BY c.user " +
                          "HAVING COUNT(c.id) >= 3";

            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();
            Map<User, Integer> statMap = new HashMap<>();

            for (Object[] row : results) {
                User user = (User) row[0];
                // Chuyển PersistentSet của Hibernate thành HashSet tiêu chuẩn của Java để Client đọc được
                user.setResearchAreas(new HashSet<>(user.getResearchAreas()));
                
                int count = ((Number) row[1]).intValue();
                statMap.put(user, count);
            }
            return statMap;
        } finally {
            em.close();
        }
    }
}