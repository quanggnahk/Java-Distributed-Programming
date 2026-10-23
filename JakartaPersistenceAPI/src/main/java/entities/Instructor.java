package entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DiscriminatorValue("Instructor")
public class Instructor extends Person {

    public LocalDateTime getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDateTime hireDate) {
		this.hireDate = hireDate;
	}

	public OfficeAssignment getOfficeAssignment() {
		return officeAssignment;
	}

	public void setOfficeAssignment(OfficeAssignment officeAssignment) {
		this.officeAssignment = officeAssignment;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	@Column(name = "HireDate")
    private LocalDateTime hireDate;

    @OneToOne(mappedBy = "instructor", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private OfficeAssignment officeAssignment;

    @ManyToMany
    @JoinTable(
        name = "CourseInstructor",
        joinColumns = @JoinColumn(name = "PersonID"),
        inverseJoinColumns = @JoinColumn(name = "CourseID")
    )
    private Set<Course> courses;

    public Instructor() {}
}