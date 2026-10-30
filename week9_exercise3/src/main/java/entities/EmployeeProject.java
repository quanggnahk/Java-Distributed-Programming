package entities;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Employee_Project")
@IdClass(EmployeeProjectId.class)
public class EmployeeProject implements Serializable {
    public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne(cascade = CascadeType.ALL) // Cascade để tự động lưu Employee nếu chưa có trong DB
    @JoinColumn(name = "employeeID")
    private Employee employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "projectID")
    private Project project;

    private int hours;

    public EmployeeProject() {}

    public EmployeeProject(Employee employee, Project project, int hours) {
        this.employee = employee;
        this.project = project;
        this.hours = hours;
    }
}