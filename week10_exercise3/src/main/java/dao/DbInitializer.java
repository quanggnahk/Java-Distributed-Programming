package dao;

import entity.*;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;

public class DbInitializer {
    public static void seedData(EntityManager em) {
        Long newsCount = em.createQuery("SELECT COUNT(n) FROM News n", Long.class).getSingleResult();
        if (newsCount > 0) return; // Nếu đã có News thì không nạp lại

        try {
            em.getTransaction().begin();

            // 1. Tạo 2 User mẫu
            Address addr1 = new Address("Hanoi", "HN", "100 Hoang Quoc Viet", "100000");
            User u1 = new User("nva_" + System.currentTimeMillis() + "@gmail.com", "Nguyen Van A", "123456", addr1);
            u1.getResearchAreas().add("Technology");

            Address addr2 = new Address("HCMC", "SG", "12 Nguyen Van Bao", "700000");
            User u2 = new User("nvb_" + System.currentTimeMillis() + "@gmail.com", "Nguyen Van B", "123456", addr2);
            u2.getResearchAreas().add("AI");

            em.persist(u1);
            em.persist(u2);

            // 2. Tạo 3 bài viết News mẫu
            News n1 = new News("AI Trend 2026", "Content about Artificial Intelligence...", LocalDateTime.now());
            n1.getNewsCategories().add("Technology");
            n1.getTags().add("AI");
            n1.getTags().add("technology");

            News n2 = new News("Java 21 Socket Tutorial", "Content about Java Socket network programming...", LocalDateTime.now());
            n2.getNewsCategories().add("Technology");
            n2.getTags().add("java");
            n2.getTags().add("technology");

            News n3 = new News("Daily World News", "General news around the world...", LocalDateTime.now());
            n3.getNewsCategories().add("Society");
            n3.getTags().add("news");

            em.persist(n1);
            em.persist(n2);
            em.persist(n3);

            // 3. Tạo Comment (u1 comment 3 lần để thỏa điều kiện getStatistics >= 3)
            Comment c1 = new Comment(u1, n1, "Bai viet AI rat hay!", LocalDateTime.now());
            Comment c2 = new Comment(u1, n2, "Cam on tac gia ve bai hieu biet Java", LocalDateTime.now());
            Comment c3 = new Comment(u1, n3, "Thong tin tin tuc rat tot", LocalDateTime.now());
            Comment c4 = new Comment(u2, n1, "Binh luan tu User B", LocalDateTime.now());

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(c4);

            em.getTransaction().commit();
            System.out.println("[DB Seeder] Đã nạp thành công dữ liệu mẫu News & Comments!");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}