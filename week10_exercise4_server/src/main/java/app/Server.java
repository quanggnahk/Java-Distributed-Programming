package app;

import dao.ProjectDao;
import dao.ProjectDaoImpl;
import entity.Employee;
import entity.Project;
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
            System.out.println("Project Management Server running on port 9999...");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("news_pu");

            try {
                EntityManager em = emf.createEntityManager();
                dao.DbInitializer.seedData(em);
                em.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket, emf)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final ProjectDao projectDao;

    public ClientHandler(Socket socket, EntityManagerFactory emf) {
        this.socket = socket;
        this.projectDao = new ProjectDaoImpl(emf);
    }

    @Override
    public void run() {
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!socket.isClosed()) {
                String command;
                try {
                    Object obj = ois.readObject();
                    if (obj == null) break;
                    command = (String) obj;
                } catch (EOFException | SocketException e) {
                    break;
                }

                switch (command) {
                    case "ADD_PROJECT":
                        try {
                            Project prj = (Project) ois.readObject();
                            boolean res = projectDao.addProject(prj);
                            oos.writeObject(res);
                        } catch (Exception e) {
                            e.printStackTrace();
                            oos.writeObject(false);
                        }
                        oos.flush();
                        break;

                    case "GET_PROJECT":
                        try {
                            String name = (String) ois.readObject();
                            Project prj = projectDao.getProject(name);
                            oos.writeObject(prj);
                        } catch (Exception e) {
                            e.printStackTrace();
                            oos.writeObject(null);
                        }
                        oos.flush();
                        break;

                    case "GET_PROJECTS_BY_EMAIL":
                        try {
                            String email = (String) ois.readObject();
                            List<Project> list = projectDao.getProjects(email);
                            oos.writeObject(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                            oos.writeObject(new ArrayList<Project>());
                        }
                        oos.flush();
                        break;

                    case "GET_EMPLOYEES_BY_NAME":
                        try {
                            String name = (String) ois.readObject();
                            List<Employee> list = projectDao.getEmployeeByName(name);
                            oos.writeObject(list);
                        } catch (Exception e) {
                            e.printStackTrace();
                            oos.writeObject(new ArrayList<Employee>());
                        }
                        oos.flush();
                        break;

                    case "GET_STATISTICS":
                        try {
                            Map<Employee, Object[]> stats = projectDao.getStatistics();
                            oos.writeObject(stats);
                        } catch (Exception e) {
                            e.printStackTrace();
                            oos.writeObject(new HashMap<Employee, Object[]>());
                        }
                        oos.flush();
                        break;

                    case "EXIT":
                        return;
                }
            }
        } catch (Exception e) {
            System.err.println("Handler exception: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }
}