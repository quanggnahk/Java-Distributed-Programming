package app;

import repositories.NewsRepository;
import repositories.NewsRepositoryImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // Thay "172.16.34.251" bằng IP thật của máy bạn trên phòng Lab
            System.setProperty("java.rmi.server.hostname", "localhost");
            
            Registry registry = LocateRegistry.createRegistry(5981);
            NewsRepository newsRepository = new NewsRepositoryImpl();
            registry.rebind("newsDAO", newsRepository);
            
            System.out.println("RMI Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}