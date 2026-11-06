package app;

import dao.NewsDao;
import dao.NewsDaoImpl;
import entity.News;
import entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Server is running on port 9999...");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("news_pu");

            // Nạp dữ liệu mẫu nếu DB trống
            try {
                EntityManager em = emf.createEntityManager();
                dao.DbInitializer.seedData(em);
                em.close();
            } catch (Exception e) {
                System.err.println("[Server Log] Seed data warning:");
                e.printStackTrace();
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket, emf)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final NewsDao newsDao;

    public ClientHandler(Socket socket, EntityManagerFactory emf) {
        this.socket = socket;
        this.newsDao = new NewsDaoImpl(emf);
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())
        ) {
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
                String command;
                try {
                    Object obj = ois.readObject();
                    if (obj == null) break;
                    command = (String) obj;
                } catch (EOFException | SocketException e) {
                    System.out.println("Client disconnected.");
                    break;
                }

                switch (command) {
                    case "ADD_USER":
                        try {
                            User userToAdd = (User) ois.readObject();
                            boolean addResult = newsDao.addUser(userToAdd);
                            oos.writeObject(addResult);
                        } catch (Exception e) {
                            System.err.println("[Server Log] Lỗi khi nhận/thêm User:");
                            e.printStackTrace();
                            oos.writeObject(false);
                        }
                        oos.flush();
                        break;

                    case "GET_NEWS":
                        try {
                            String searchVal = (String) ois.readObject();
                            List<News> newsList = newsDao.getNewsByTagsOrNewsCategories(searchVal);
                            oos.writeObject(newsList);
                        } catch (Exception e) {
                            System.err.println("[Server Log] Lỗi khi tìm kiếm News:");
                            e.printStackTrace();
                            oos.writeObject(new ArrayList<News>());
                        }
                        oos.flush();
                        break;

                    case "GET_STATS":
                        try {
                            Map<User, Integer> stats = newsDao.getStatistics();
                            oos.writeObject(stats);
                        } catch (Exception e) {
                            System.err.println("[Server Log] Lỗi khi thống kê:");
                            e.printStackTrace();
                            oos.writeObject(new HashMap<User, Integer>());
                        }
                        oos.flush();
                        break;

                    case "EXIT":
                        return;
                }
            }
        } catch (Exception e) {
            System.err.println("Client handler exception: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {}
        }
    }
}