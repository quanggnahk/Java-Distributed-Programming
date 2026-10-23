package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryUtil {
    
    private static final EntityManagerFactory entityManageFactory;
    
    static {
        try {
            entityManageFactory = Persistence.createEntityManagerFactory("week7-8");
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed. " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static EntityManager getEntityManager() {
        return entityManageFactory.createEntityManager();
    }
    
    public static void close() {
        if (entityManageFactory != null && entityManageFactory.isOpen()) {
            entityManageFactory.close();
        }
    }
}