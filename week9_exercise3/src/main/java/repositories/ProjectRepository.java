package repositories;

import entities.Employee;
import entities.Project;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface ProjectRepository extends Remote {
    boolean addProject(Project prj) throws RemoteException;
    Project getProject(String name) throws RemoteException;
    List<Project> getProjects(String empl_email) throws RemoteException;
    List<Employee> getEmployeeByName(String aValue) throws RemoteException;
    Map<Employee, Object[]> getStatistics() throws RemoteException;
}