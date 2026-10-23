package entities;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "OfficeAssignment")
public class OfficeAssignment {

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	@Id
    @Column(name = "InstructorID")
    private int id;

    @Column(name = "Location")
    private String location;

    @Column(name = "Timestamp")
    private Timestamp timestamp;

    @OneToOne
    @MapsId 
    @JoinColumn(name = "InstructorID")
    private Instructor instructor;

    public OfficeAssignment() {}
}