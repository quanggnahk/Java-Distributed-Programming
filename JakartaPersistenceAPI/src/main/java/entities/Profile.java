package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	@Id
    @Column(name = "staff_id")
    private long id;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToOne
    @MapsId 
    @JoinColumn(name = "staff_id")
    private Staff staff;

    public Profile() {}
}