package App;

import dao.BikeStoreQueries;
import dao.GenericDAO;
import entities.Brand;
import entities.Customer;
import entities.Product;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("week7-8");
        try {
            // ===== Câu 1: CRUD (ví dụ với Product và Customer) =====
            GenericDAO<Product, Integer> productDAO = new GenericDAO<>(factory, Product.class);
            GenericDAO<Customer, Integer> customerDAO = new GenericDAO<>(factory, Customer.class);

            System.out.println("Tổng số sản phẩm : " + productDAO.getAll().size());
            System.out.println("Tổng số khách    : " + customerDAO.getAll().size());
            System.out.println("Product id = 1   : " + (productDAO.findById(1) != null));

            // Demo CRUD đầy đủ trên Brand: tạo -> sửa -> tìm -> xóa (không để lại dữ liệu thừa)
            GenericDAO<Brand, Integer> brandDAO = new GenericDAO<>(factory, Brand.class);
            Brand b = new Brand();
            b.setName("Test Brand");
            brandDAO.create(b);                       // Create (id identity tự sinh)
            System.out.println("Đã tạo brand id = " + b.getId());
            b.setName("Test Brand 2");
            brandDAO.update(b);                       // Update
            System.out.println("Sau update: " + brandDAO.findById(b.getId()).getName()); // Find by Id
            System.out.println("Số brand: " + brandDAO.getAll().size());                 // Get All
            System.out.println("Đã xóa: " + brandDAO.delete(b.getId()));                 // Delete

            BikeStoreQueries q = new BikeStoreQueries(factory);

            // ===== Câu 2 =====
            System.out.println("Sản phẩm giá cao nhất: " + q.findMostExpensiveProducts().size());

            // ===== Câu 3 =====
            System.out.println("Sản phẩm chưa bán được: " + q.findProductsNeverSold().size());

            // ===== Câu 4 =====
            System.out.println("Khách hàng theo bang: " + q.getNumberCustomerByState());

            // ===== Câu 5 =====
            System.out.println("Tổng tiền đơn hàng 1: " + q.getOrderTotal(1));

            // ===== Câu 6 =====
            System.out.println("Số khách (câu 6): " + q.getOrdersByCustomers().size());

            // ===== Câu 7 =====
            System.out.println("Số sản phẩm đã bán (câu 7): " + q.getTotalProduct().size());

            // ===== Câu 8 =====
            System.out.println("Doanh thu 2016-01-01: " + q.getTotalRevenueByDate(LocalDate.of(2016, 1, 1)));

            // ===== Câu 10 =====
            System.out.println("Doanh thu theo tháng: " + q.getRevenueByMonth());
            System.out.println("Doanh thu theo năm  : " + q.getRevenueByYear());

            // ===== Câu 9 (XÓA DỮ LIỆU - chỉ chạy khi chắc chắn) =====
            // System.out.println("Đã xóa " + q.deleteCustomersWithoutOrders() + " khách hàng");
        } finally {
            factory.close();
        }
    }
}