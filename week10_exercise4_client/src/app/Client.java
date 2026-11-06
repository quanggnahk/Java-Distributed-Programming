package app;

import entity.Employee;
import entity.Project;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ) {
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to Server!");

            // 1. Test addProject
            System.out.println("\n--- 1. Testing addProject ---");
            Project newPrj = new Project("AI Chatbot System", "AI Application");
            newPrj.getVersions().add("v1.0-alpha");

            oos.writeObject("ADD_PROJECT");
            oos.writeObject(newPrj);
            oos.flush();
            System.out.println("Add Project Result: " + ois.readObject());

            // 2. Test getProject
            System.out.println("\n--- 2. Testing getProject ---");
            oos.writeObject("GET_PROJECT");
            oos.writeObject("ERP System");
            oos.flush();
            Project foundPrj = (Project) ois.readObject();
            if (foundPrj != null) {
                System.out.println("Found Project: " + foundPrj.getProjectName() + " | Type: " + foundPrj.getType());
            } else {
                System.out.println("Project not found!");
            }

            // 3. Test getProjects by Email
            System.out.println("\n--- 3. Testing getProjects by Email ---");
            oos.writeObject("GET_PROJECTS_BY_EMAIL");
            oos.writeObject("khang@gmail.com");
            oos.flush();
            @SuppressWarnings("unchecked")
            List<Project> prjList = (List<Project>) ois.readObject();
            System.out.println("Projects worked by khang@gmail.com: " + prjList.size());
            for (Project p : prjList) {
                System.out.println("- " + p.getProjectName());
            }

            // 4. Test getEmployeeByName
            System.out.println("\n--- 4. Testing getEmployeeByName ---");
            oos.writeObject("GET_EMPLOYEES_BY_NAME");
            oos.writeObject("Khang");
            oos.flush();
            @SuppressWarnings("unchecked")
            List<Employee> empList = (List<Employee>) ois.readObject();
            System.out.println("Employees matching 'Khang': " + empList.size());
            for (Employee e : empList) {
                System.out.println("- " + e.getFirstName() + " " + e.getLastName() + " | Email: " + e.getEmail());
            }

            // 5. Test getStatistics (Lập bảng lương)
            System.out.println("\n--- 5. Testing getStatistics (Payroll) ---");
            oos.writeObject("GET_STATISTICS");
            oos.flush();
            @SuppressWarnings("unchecked")
            Map<Employee, Object[]> stats = (Map<Employee, Object[]>) ois.readObject();
            for (Map.Entry<Employee, Object[]> entry : stats.entrySet()) {
                Employee emp = entry.getKey();
                Object[] data = entry.getValue();
                long totalHours = (long) data[0];
                double totalSalary = (double) data[1];
                System.out.println("Employee: " + emp.getFirstName() + " " + emp.getLastName() + 
                                   " | Total Hours: " + totalHours + "h | Total Salary: $" + totalSalary);
            }

            // Thoát
            oos.writeObject("EXIT");
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}