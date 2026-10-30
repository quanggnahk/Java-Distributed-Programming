package app;

import entities.Employee;
import entities.Project;
import repositories.ProjectRepository;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 5981);
            ProjectRepository repo = (ProjectRepository) registry.lookup("projectDAO");

            // ======= TEST 2a: Thêm Project và EmployeeProject =======[cite: 8]
            System.out.println("--- 2a. Thêm Project ---");
            Employee emp1 = new Employee("Khang", "Dang", "khang@gmail.com", "0901234567");
            Employee emp2 = new Employee("Nguyen", "Van A", "nva@gmail.com", "0987654321");

            Project prj = new Project("He thong IoT", "Phan cung & Web");
            prj.setVersions(Set.of("v1.0", "v1.1"));
            
            // Dùng hàm addDetail theo sơ đồ lớp
            prj.addDetail(emp1, 100); 
            prj.addDetail(emp2, 150);

            boolean isAdded = repo.addProject(prj);
            System.out.println("Thêm dự án thành công: " + isAdded);

            // ======= TEST 2b: Tìm dự án theo tên =======[cite: 8]
            System.out.println("\n--- 2b. Tìm dự án theo tên ---");
            Project foundPrj = repo.getProject("He thong IoT");
            System.out.println(foundPrj);

            // ======= TEST 2c: Tìm dự án theo Email =======[cite: 8]
            System.out.println("\n--- 2c. Tìm dự án theo Email ---");
            List<Project> prjsByEmail = repo.getProjects("khang@gmail.com");
            prjsByEmail.forEach(System.out::println);

            // ======= TEST 2d: Text Search Employee =======[cite: 8]
            System.out.println("\n--- 2d. Tìm nhân viên (Khang) ---");
            List<Employee> emps = repo.getEmployeeByName("Khang");
            emps.forEach(System.out::println);

            // ======= TEST 2e: Bảng lương =======[cite: 9]
            System.out.println("\n--- 2e. Thống kê Bảng Lương ---");
            Map<Employee, Object[]> stats = repo.getStatistics();
            stats.forEach((employee, data) -> {
                Long hours = (Long) data[0];
                Double salary = (Double) data[1];
                System.out.println(employee.getFirstName() + " | Giờ làm: " + hours + " | Lương: $" + salary);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}