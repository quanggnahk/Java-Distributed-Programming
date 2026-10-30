package entities;

import java.io.Serializable;
import java.util.Objects;

public class EmployeeProjectId implements Serializable {
    public Long getEmployee() {
		return employee;
	}

	public void setEmployee(Long employee) {
		this.employee = employee;
	}

	public Long getProject() {
		return project;
	}

	public void setProject(Long project) {
		this.project = project;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;
    
    private Long employee; 
    private Long project;

    public EmployeeProjectId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeProjectId that = (EmployeeProjectId) o;
        return Objects.equals(employee, that.employee) && Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, project);
    }
}