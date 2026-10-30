package app;

import entities.Address;
import entities.News;
import entities.User;
import repositories.NewsRepository;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RMIClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        // Kết nối tới Server IP và Port
        Registry registry = LocateRegistry.getRegistry("localhost", 5981);

        NewsRepository newsRepository = (NewsRepository) registry.lookup("newsDAO");

        // ======= TEST 2A: THÊM USER =======[cite: 6]
        System.out.println("--- Test 2a: Thêm User ---");
        Address addr = new Address("Nguyen Van Bao", "Go Vap", "HCM", "700000");
        User user = new User("Khang Dang", "khang@gmail.com", "123456", addr);
        user.setResearchAreas(Set.of("AI", "IoT", "Embedded System"));

        boolean isAdded = newsRepository.addUser(user);
        System.out.println("Thêm user thành công? " + isAdded);

        // ======= TEST 2B: TÌM KIẾM TIN TỨC =======[cite: 6]
        System.out.println("\n--- Test 2b: Tìm kiếm tin tức theo Tag / Category ---");
        List<News> newsList = newsRepository.getNewsByTagsOrNewsCategories("Tech");
        newsList.forEach(System.out::println);

        // ======= TEST 2C: THỐNG KÊ (Dùng Stream API theo mẫu giảng viên) =======[cite: 7]
        System.out.println("\n--- Test 2c: Thống kê User có >= 3 bình luận ---");
        Map<User, Integer> stats = newsRepository.getStatistics();
        stats.entrySet().stream().forEach(entry -> {
            System.out.println(entry.getKey().getUserName() + " --- Số bình luận: " + entry.getValue());
        });
    }
}