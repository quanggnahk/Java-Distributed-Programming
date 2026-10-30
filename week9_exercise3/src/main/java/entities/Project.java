package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Projects")
public class Project implements Serializable {
    public Long getProjectID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<String> getVersions() {
		return versions;
	}

	public void setVersions(Set<String> versions) {
		this.versions = versions;
	}

	public Set<EmployeeProject> getEmpls() {
		return empls;
	}

	public void setEmpls(Set<EmployeeProject> empls) {
		this.empls = empls;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectID;

    private String projectName;
    private String type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Project_Versions", joinColumns = @JoinColumn(name = "projectID"))
    private Set<String> versions = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<EmployeeProject> empls = new HashSet<>();

    public Project() {}

    public Project(String projectName, String type) {
        this.projectName = projectName;
        this.type = type;
    }

    // Hàm thêm chi tiết theo đúng sơ đồ UML
    public void addDetail(Employee emp, int hours) {
        EmployeeProject ep = new EmployeeProject(emp, this, hours);
        this.empls.add(ep);
    }

    @Override
    public String toString() {
        return "Project{id=" + projectID + ", name='" + projectName + "'}";
    }
}