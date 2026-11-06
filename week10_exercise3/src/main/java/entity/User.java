package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements Serializable {
    // BẮT BUỘC KHAI BÁO DÒNG NÀY Ở CẢ CLIENT VÀ SERVER
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String userName;
    private String userPassword;

    @Embedded
    private Address userAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_research_areas", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "research_area")
    private Set<String> researchAreas = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private transient Set<Comment> comments = new HashSet<>();

    public User() {}

    public User(String userEmail, String userName, String userPassword, Address userAddress) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    public Address getUserAddress() { return userAddress; }
    public void setUserAddress(Address userAddress) { this.userAddress = userAddress; }
    public Set<String> getResearchAreas() { return researchAreas; }
    public void setResearchAreas(Set<String> researchAreas) { this.researchAreas = researchAreas; }
    public Set<Comment> getComments() { return comments; }
    public void setComments(Set<Comment> comments) { this.comments = comments; }
}