package repositories;

import entities.Order;
import entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class OrderRepositoryImpl extends UnicastRemoteObject implements OrderRepository {
    private EntityManagerFactory emf;

    public OrderRepositoryImpl() throws RemoteException {
        super();
        emf = Persistence.createEntityManagerFactory("OrderPU"); // Nhớ cấu hình tên này trong persistence.xml
    }

    @Override
    public boolean addProduct(Product p) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean createOrder(Order order) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(order); // Lưu Order sẽ tự động lưu luôn các OrderDetail nhờ @ElementCollection
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Order findOrderById(int orderId) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            Order o = em.find(Order.class, orderId);
            
            // Xử lý un-proxy lớp vỏ PersistentSet của Hibernate để gửi qua RMI an toàn
            if (o != null) {
                o.setLineItems(new java.util.HashSet<>(o.getLineItems()));
            }
            return o;
        } finally {
            em.close();
        }
    }
}