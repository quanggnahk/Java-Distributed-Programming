package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User implements Serializable {
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Address getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(Address userAddress) {
		this.userAddress = userAddress;
	}

	public Set<String> getResearchAreas() {
		return researchAreas;
	}

	public void setResearchAreas(Set<String> researchAreas) {
		this.researchAreas = researchAreas;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String userName;
    private String userEmail;
    private String userPassword;

    @Embedded
    private Address userAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "User_ResearchAreas", joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> researchAreas = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public User() {}

    public User(String userName, String userEmail, String userPassword, Address userAddress) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", userName='" + userName + "'}";
    }
}