package app;

import entity.Address;
import entity.News;
import entity.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);
            
            // Tạo OOS trước và flush ngay lập tức
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to Server!");

            // =========================================================
            // TEST 1: Thêm người dùng (ADD_USER)
            // =========================================================
            System.out.println("\n--- 1. Testing Add User ---");
            Address addr = new Address("HCMC", "SG", "12 Nguyen Van Bao", "700000");
            User newUser = new User("test@gmail.com", "Khang", "123456", addr);
            newUser.getResearchAreas().add("AI");
            newUser.getResearchAreas().add("IoT");

            oos.writeObject("ADD_USER");
            oos.writeObject(newUser);
            oos.flush();

            boolean result = (boolean) ois.readObject();
            System.out.println("Result of addUser: " + result);

            // =========================================================
            // TEST 2: Tìm kiếm tin tức (GET_NEWS)
            // =========================================================
            System.out.println("\n--- 2. Testing Get News ---");
            oos.writeObject("GET_NEWS");
            oos.writeObject("technology");
            oos.flush();

            @SuppressWarnings("unchecked")
            List<News> newsList = (List<News>) ois.readObject();
            System.out.println("Found " + newsList.size() + " news.");
            for (News n : newsList) {
                System.out.println("- " + n.getNewsTitle());
            }

            // =========================================================
            // TEST 3: Lấy thống kê (GET_STATS)
            // =========================================================
            System.out.println("\n--- 3. Testing Get Statistics ---");
            oos.writeObject("GET_STATS");
            oos.flush();

            @SuppressWarnings("unchecked")
            Map<User, Integer> stats = (Map<User, Integer>) ois.readObject();
            System.out.println("Statistics (User with >=3 comments):");
            for (Map.Entry<User, Integer> entry : stats.entrySet()) {
                System.out.println("User: " + entry.getKey().getUserName() + " | News commented: " + entry.getValue());
            }

            // Kết thúc
            oos.writeObject("EXIT");
            oos.flush();

            ois.close();
            oos.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}