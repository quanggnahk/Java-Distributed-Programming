package app;

import repositories.ProjectRepository;
import repositories.ProjectRepositoryImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "localhost");
            Registry registry = LocateRegistry.createRegistry(5981);
            ProjectRepository projectRepository = new ProjectRepositoryImpl();
            registry.rebind("projectDAO", projectRepository);

            System.out.println("RMI Server đang chạy (Port 5981)...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}