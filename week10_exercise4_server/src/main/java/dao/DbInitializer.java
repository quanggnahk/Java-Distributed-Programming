package dao;

import entity.*;
import jakarta.persistence.EntityManager;

public class DbInitializer {
    public static void seedData(EntityManager em) {
        Long projectCount = em.createQuery("SELECT COUNT(p) FROM Project p", Long.class).getSingleResult();
        if (projectCount > 0) return;

        try {
            em.getTransaction().begin();

            Employee e1 = new Employee("Khang", "Dang", "khang@gmail.com", "0901234567");
            Employee e2 = new Employee("John", "Doe", "john@gmail.com", "0987654321");
            em.persist(e1);
            em.persist(e2);

            Project p1 = new Project("ERP System", "Web Application");
            p1.getVersions().add("v1.0");
            p1.getVersions().add("v2.0");

            Project p2 = new Project("Mobile Smart Garment", "IoT Mobile");
            p2.getVersions().add("v1.0");

            em.persist(p1);
            em.persist(p2);

            // Gán số giờ làm
            EmployeeProject ep1 = new EmployeeProject(e1, p1, 100);
            EmployeeProject ep2 = new EmployeeProject(e1, p2, 50);
            EmployeeProject ep3 = new EmployeeProject(e2, p1, 120);

            em.persist(ep1);
            em.persist(ep2);
            em.persist(ep3);

            em.getTransaction().commit();
            System.out.println("[DB Seeder] Nạp thành công dữ liệu mẫu Employee & Project!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}