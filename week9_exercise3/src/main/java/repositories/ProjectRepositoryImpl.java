package repositories;

import entities.Employee;
import entities.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectRepositoryImpl extends UnicastRemoteObject implements ProjectRepository {
    private EntityManagerFactory emf;

    public ProjectRepositoryImpl() throws RemoteException {
        super();
        emf = Persistence.createEntityManagerFactory("NewsPU"); // Nhớ đổi tên Unit trong persistence.xml cho khớp
    }

    @Override
    public boolean addProject(Project prj) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prj); // Do đã set cascade=ALL, nó sẽ tự động lưu luôn EmployeeProject và Employee
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Project getProject(String name) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            Project p = em.createQuery("SELECT p FROM Project p WHERE p.projectName = :name", Project.class)
                     .setParameter("name", name)
                     .getResultStream().findFirst().orElse(null);
            
            // LỘT BỎ LỚP VỎ HIBERNATE TRƯỚC KHI GỬI QUA RMI
            if (p != null) {
                // Chuyển PersistentSet về HashSet chuẩn của Java
                p.setVersions(new java.util.HashSet<>(p.getVersions()));
                
                // Set null cho danh sách nhân viên vì Client chỉ gọi in tên Project ra màn hình,
                // Không cần dùng mảng này (Tránh lỗi đệ quy vòng lặp RMI Serialize)
                p.setEmpls(null); 
            }
            return p;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> getProjects(String empl_email) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT ep.project FROM EmployeeProject ep WHERE ep.employee.email = :email";
            List<Project> projects = em.createQuery(jpql, Project.class)
                     .setParameter("email", empl_email)
                     .getResultList();
                     
            // UN-PROXY CHO TOÀN BỘ DANH SÁCH TRƯỚC KHI GỬI VỀ CLIENT
            for (Project p : projects) {
                p.setVersions(new java.util.HashSet<>(p.getVersions()));
                p.setEmpls(null); 
            }
            return projects;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Employee> getEmployeeByName(String aValue) throws RemoteException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT e FROM Employee e WHERE e.firstName LIKE :val OR e.lastName LIKE :val";
            return em.createQuery(jpql, Employee.class)
                     .setParameter("val", "%" + aValue + "%")
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<Employee, Object[]> getStatistics() throws RemoteException {
        EntityManager em = emf.createEntityManager();
        Map<Employee, Object[]> result = new HashMap<>();
        try {
            // Lấy Employee và Tổng số giờ
            String jpql = "SELECT ep.employee, SUM(ep.hours) FROM EmployeeProject ep GROUP BY ep.employee";
            List<Object[]> list = em.createQuery(jpql, Object[].class).getResultList();

            for (Object[] row : list) {
                Employee emp = (Employee) row[0];
                Long totalHours = (Long) row[1];
                // Tính tiền lương: 1 giờ = 6$
                double salary = totalHours * 6.0; 
                result.put(emp, new Object[]{totalHours, salary});
            }
            return result;
        } finally {
            em.close();
        }
    }
}