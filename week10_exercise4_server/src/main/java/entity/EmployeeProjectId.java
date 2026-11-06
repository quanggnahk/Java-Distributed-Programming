package entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeeProjectId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long employeeID;
    private Long projectID;

    public EmployeeProjectId() {}

    public EmployeeProjectId(Long employeeID, Long projectID) {
        this.employeeID = employeeID;
        this.projectID = projectID;
    }

    public Long getEmployeeID() { return employeeID; }
    public void setEmployeeID(Long employeeID) { this.employeeID = employeeID; }
    public Long getProjectID() { return projectID; }
    public void setProjectID(Long projectID) { this.projectID = projectID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeProjectId that = (EmployeeProjectId) o;
        return Objects.equals(employeeID, that.employeeID) && Objects.equals(projectID, that.projectID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeID, projectID);
    }
}