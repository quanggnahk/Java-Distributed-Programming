package app;

import entities.Order;
import entities.OrderDetail;
import entities.Product;
import repositories.OrderRepository;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5981);
            OrderRepository repo = (OrderRepository) registry.lookup("orderDAO");

            // 1. Tạo sẵn một vài Sản phẩm (Product) vào Database
            System.out.println("--- Khởi tạo Sản phẩm ---");
            Product p1 = new Product("P001", "Laptop Dell", 1500.0);
            Product p2 = new Product("P002", "Chuot Logitech", 50.0);
            repo.addProduct(p1);
            repo.addProduct(p2);
            System.out.println("Đã thêm sản phẩm thành công.");

            // 2. Tạo đơn đặt hàng từ xa[cite: 11]
            System.out.println("\n--- Tạo Đơn hàng mới ---");
            Order myOrder = new Order(1001, new Date());
            
            // Gọi phương thức addLineItem để thêm sản phẩm vào hóa đơn[cite: 12]
            myOrder.addLineItem(p1, 2); // 2 Laptop
            myOrder.addLineItem(p2, 5); // 5 Chuột
            
            boolean isOrderCreated = repo.createOrder(myOrder);
            System.out.println("Tạo đơn hàng 1001 thành công? " + isOrderCreated);

            // 3. Tìm kiếm đơn hàng khi biết mã đơn hàng[cite: 11]
            System.out.println("\n--- Tìm kiếm Đơn hàng ---");
            Order foundOrder = repo.findOrderById(1001);
            
            if (foundOrder != null) {
                System.out.println("Thông tin đơn hàng: " + foundOrder.toString());
                System.out.println("Tổng tiền hóa đơn (totalCharge): $" + foundOrder.calcTotalCharge()); //[cite: 12]
                System.out.println("Chi tiết các món hàng:");
                for (OrderDetail item : foundOrder.getLineItems()) {
                    System.out.println(" - " + item.toString());
                }
            } else {
                System.out.println("Không tìm thấy đơn hàng!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}