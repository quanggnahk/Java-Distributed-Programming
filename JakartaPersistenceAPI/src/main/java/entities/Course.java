package entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Course")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Course {

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public Department1 getDepartment() {
		return department;
	}

	public void setDepartment(Department1 department) {
		this.department = department;
	}

	public Set<Instructor> getInstructors() {
		return instructors;
	}

	public void setInstructors(Set<Instructor> instructors) {
		this.instructors = instructors;
	}

	public Set<StudentGrade> getStudentGrades() {
		return studentGrades;
	}

	public void setStudentGrades(Set<StudentGrade> studentGrades) {
		this.studentGrades = studentGrades;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseID")
    protected int id;

    @Column(name = "Title")
    protected String title;

    @Column(name = "Credits")
    protected int credits;

    @ManyToOne
    @JoinColumn(name = "DepartmentID")
    protected Department1 department;

    @ManyToMany(mappedBy = "courses")
    protected Set<Instructor> instructors;

    @OneToMany(mappedBy = "course")
    protected Set<StudentGrade> studentGrades;

    public Course() {}
}