package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "employee_projects")
public class EmployeeProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EmployeeProjectId id;

    private int hours;

    @ManyToOne
    @MapsId("employeeID")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @MapsId("projectID")
    @JoinColumn(name = "project_id")
    private Project project;

    public EmployeeProject() {}

    public EmployeeProject(Employee employee, Project project, int hours) {
        this.employee = employee;
        this.project = project;
        this.hours = hours;
        this.id = new EmployeeProjectId(employee.getEmployeeID(), project.getProjectID());
    }

    public EmployeeProjectId getId() { return id; }
    public void setId(EmployeeProjectId id) { this.id = id; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}