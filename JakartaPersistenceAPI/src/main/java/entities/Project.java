package entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public Set<Staff> getStaffs() {
		return staffs;
	}

	public void setStaffs(Set<Staff> staffs) {
		this.staffs = staffs;
	}

	@Id
    @Column(name = "project_id", length = 50)
    private String id;

    @Column(name = "project_name", length = 255)
    private String name;

    @Column(name = "budget")
    private double budget;

    @ManyToMany(mappedBy = "projects")
    private Set<Staff> staffs;

    public Project() {}
}