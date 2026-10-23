package entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "staffs")
public class Staff {

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Set<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Set<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	@Id
    @Column(name = "staff_id")
    private long id;

    @Column(name = "staff_name", columnDefinition = "nvarchar(100)")
    private String name; 

    @Column(name = "age")
    private int age;

    @Column(name = "refers", length = 255)
    private String references;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;

    @ManyToMany
    @JoinTable(
        name = "staff_projects",
        joinColumns = @JoinColumn(name = "staff_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;

    @ElementCollection
    @CollectionTable(
        name = "phones", 
        joinColumns = @JoinColumn(name = "staff_id")
    )
    @Column(name = "number", length = 255)
    private Set<String> phoneNumbers;

    public Staff() {}

    public Staff(long id, String name, String references, int age) {
        this.id = id;
        this.name = name;
        this.references = references;
        this.age = age;
    }
}