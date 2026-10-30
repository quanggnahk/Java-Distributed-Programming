package app;

import repositories.OrderRepository;
import repositories.OrderRepositoryImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            Registry registry = LocateRegistry.createRegistry(5981);
            OrderRepository orderRepo = new OrderRepositoryImpl();
            registry.rebind("orderDAO", orderRepo);
            System.out.println("Order Processing Server đang chạy trên cổng 5981...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}