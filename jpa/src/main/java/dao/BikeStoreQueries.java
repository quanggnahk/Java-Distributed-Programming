package dao;

import entities.Customer;
import entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BikeStoreQueries {

    /** Thành tiền một dòng hóa đơn (alias của OrderItem phải là "oi"). */
    private static final String LINE_TOTAL = "oi.quantity * oi.listPrice * (1 - oi.discount)";

    private final EntityManagerFactory emf;

    public BikeStoreQueries(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // ----------------- tiện ích -----------------

    private <R> R query(Function<EntityManager, R> work) {
        EntityManager em = emf.createEntityManager();
        try {
            return work.apply(em);
        } finally {
            em.close();
        }
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

    // ----------------- Câu 2 -----------------
    /** Danh sách sản phẩm có giá cao nhất (có thể nhiều sản phẩm cùng giá cao nhất). */
    public List<Product> findMostExpensiveProducts() {
        return query(em -> em.createQuery(
                "SELECT p FROM Product p "
              + "WHERE p.listPrice = (SELECT MAX(p2.listPrice) FROM Product p2)",
                Product.class).getResultList());
    }

    // ----------------- Câu 3 -----------------
    /** Danh sách sản phẩm chưa bán được lần nào (không xuất hiện trong order_items). */
    public List<Product> findProductsNeverSold() {
        return query(em -> em.createQuery(
                "SELECT p FROM Product p "
              + "WHERE NOT EXISTS (SELECT 1 FROM OrderItem oi WHERE oi.product = p)",
                Product.class).getResultList());
    }

    // ----------------- Câu 4 -----------------
    /** Thống kê số khách hàng theo từng bang. */
    public Map<String, Integer> getNumberCustomerByState() {
        return query(em -> {
            List<Object[]> rows = em.createQuery(
                    "SELECT c.address.state, COUNT(c) FROM Customer c "
                  + "GROUP BY c.address.state ORDER BY c.address.state",
                    Object[].class).getResultList();
            Map<String, Integer> result = new LinkedHashMap<>();
            for (Object[] r : rows) {
                result.put((String) r[0], ((Number) r[1]).intValue());
            }
            return result;
        });
    }

    // ----------------- Câu 5 -----------------
    /** Tổng tiền của một đơn hàng = SUM(quantity * list_price * (1 - discount)). */
    public double getOrderTotal(int orderId) {
        return query(em -> {
            Double total = em.createQuery(
                    "SELECT SUM(" + LINE_TOTAL + ") FROM OrderItem oi "
                  + "WHERE oi.order.id = :id", Double.class)
                    .setParameter("id", orderId)
                    .getSingleResult();
            return total == null ? 0.0 : total;
        });
    }

    // ----------------- Câu 6 -----------------
    /** Đếm số đơn hàng của từng khách hàng (khách chưa mua sẽ có giá trị 0). */
    public Map<Customer, Integer> getOrdersByCustomers() {
        return query(em -> {
            List<Object[]> rows = em.createQuery(
                    "SELECT c, (SELECT COUNT(o) FROM Order o WHERE o.customer = c) "
                  + "FROM Customer c", Object[].class).getResultList();
            Map<Customer, Integer> result = new LinkedHashMap<>();
            for (Object[] r : rows) {
                result.put((Customer) r[0], ((Number) r[1]).intValue());
            }
            return result;
        });
    }

    // ----------------- Câu 7 -----------------
    /** Tổng số lượng đã bán của từng sản phẩm (chỉ gồm sản phẩm đã bán). */
    public Map<Product, Integer> getTotalProduct() {
        return query(em -> {
            List<Object[]> rows = em.createQuery(
                    "SELECT p, (SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product = p) "
                  + "FROM Product p", Object[].class).getResultList();
            Map<Product, Integer> result = new LinkedHashMap<>();
            for (Object[] r : rows) {
                if (r[1] != null) {
                    result.put((Product) r[0], ((Number) r[1]).intValue());
                }
            }
            return result;
        });
    }

    // ----------------- Câu 8 -----------------
    /** Tổng tiền của tất cả hóa đơn trong một ngày. */
    public double getTotalRevenueByDate(LocalDate date) {
        return query(em -> {
            Double total = em.createQuery(
                    "SELECT SUM(" + LINE_TOTAL + ") FROM OrderItem oi "
                  + "WHERE oi.order.orderDate = :date", Double.class)
                    .setParameter("date", date)
                    .getSingleResult();
            return total == null ? 0.0 : total;
        });
    }

    // ----------------- Câu 9 -----------------
    /** Xóa tất cả khách hàng chưa mua hàng. Trả về số khách đã xóa. */
    public int deleteCustomersWithoutOrders() {
        return inTransaction(em -> em.createQuery(
                "DELETE FROM Customer c "
              + "WHERE NOT EXISTS (SELECT 1 FROM Order o WHERE o.customer = c)")
                .executeUpdate());
    }

    // ----------------- Câu 10 -----------------
    /** Thống kê tổng tiền hóa đơn theo THÁNG. Khóa có dạng "2016-03". */
    public Map<String, Double> getRevenueByMonth() {
        return query(em -> {
            List<Object[]> rows = em.createQuery(
                    "SELECT YEAR(o.orderDate), MONTH(o.orderDate), SUM(" + LINE_TOTAL + ") "
                  + "FROM OrderItem oi JOIN oi.order o "
                  + "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) "
                  + "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)",
                    Object[].class).getResultList();
            Map<String, Double> result = new LinkedHashMap<>();
            for (Object[] r : rows) {
                String key = String.format("%04d-%02d",
                        ((Number) r[0]).intValue(), ((Number) r[1]).intValue());
                result.put(key, ((Number) r[2]).doubleValue());
            }
            return result;
        });
    }

    /** Thống kê tổng tiền hóa đơn theo NĂM. */
    public Map<Integer, Double> getRevenueByYear() {
        return query(em -> {
            List<Object[]> rows = em.createQuery(
                    "SELECT YEAR(o.orderDate), SUM(" + LINE_TOTAL + ") "
                  + "FROM OrderItem oi JOIN oi.order o "
                  + "GROUP BY YEAR(o.orderDate) ORDER BY YEAR(o.orderDate)",
                    Object[].class).getResultList();
            Map<Integer, Double> result = new LinkedHashMap<>();
            for (Object[] r : rows) {
                result.put(((Number) r[0]).intValue(), ((Number) r[1]).doubleValue());
            }
            return result;
        });
    }
}