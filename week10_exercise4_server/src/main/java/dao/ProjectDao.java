package dao;

import entity.Employee;
import entity.Project;
import java.util.List;
import java.util.Map;

public interface ProjectDao {
    boolean addProject(Project prj);
    Project getProject(String name);
    List<Project> getProjects(String empl_email);
    List<Employee> getEmployeeByName(String aValue);
    Map<Employee, Object[]> getStatistics();
}