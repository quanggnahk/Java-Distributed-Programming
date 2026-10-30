package repositories;

import entities.Order;
import entities.Product;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OrderRepository extends Remote {
    // Lưu sản phẩm vào DB (Tiện ích hỗ trợ)
    boolean addProduct(Product p) throws RemoteException;
    
    // Yêu cầu: Tạo đơn hàng từ xa[cite: 11]
    boolean createOrder(Order order) throws RemoteException;
    
    // Yêu cầu: Tìm kiếm đơn hàng khi biết mã đơn hàng[cite: 11]
    Order findOrderById(int orderId) throws RemoteException;
}