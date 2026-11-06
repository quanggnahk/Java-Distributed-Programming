package dao;

import entity.Employee;
import entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProjectDaoImpl implements ProjectDao {
    private final EntityManagerFactory emf;

    public ProjectDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean addProject(Project prj) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prj);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Project getProject(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT p FROM Project p WHERE p.projectName = :name";
            List<Project> list = em.createQuery(jpql, Project.class)
                                  .setParameter("name", name)
                                  .getResultList();
            if (list.isEmpty()) return null;
            
            Project p = list.get(0);
            p.setVersions(new HashSet<>(p.getVersions())); // Tránh PersistentSet
            return p;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> getProjects(String empl_email) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT ep.project FROM EmployeeProject ep WHERE LOWER(ep.employee.email) = LOWER(:email)";
            List<Project> list = em.createQuery(jpql, Project.class)
                                  .setParameter("email", empl_email)
                                  .getResultList();
            
            for (Project p : list) {
                p.setVersions(new HashSet<>(p.getVersions()));
            }
            return list;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Employee> getEmployeeByName(String aValue) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT DISTINCT e FROM Employee e " +
                          "WHERE LOWER(e.firstName) LIKE LOWER(:val) OR LOWER(e.lastName) LIKE LOWER(:val)";
            return em.createQuery(jpql, Employee.class)
                     .setParameter("val", "%" + aValue + "%")
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Map<Employee, Object[]> getStatistics() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT ep.employee, SUM(ep.hours) FROM EmployeeProject ep GROUP BY ep.employee";
            List<Object[]> results = em.createQuery(jpql, Object[].class).getResultList();
            
            Map<Employee, Object[]> resultMap = new HashMap<>();
            for (Object[] row : results) {
                Employee emp = (Employee) row[0];
                long totalHours = ((Number) row[1]).longValue();
                double totalSalary = totalHours * 6.0; // 6$ mỗi giờ
                
                resultMap.put(emp, new Object[]{totalHours, totalSalary});
            }
            return resultMap;
        } finally {
            em.close();
        }
    }
}