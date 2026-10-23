package entities;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "group_id")
    private int groupId;

    @Column(name = "name", length = 45)
    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users;

    public Group() {}

    public Group(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public int getGroupId() { return groupId; }
    public void setGroupId(int groupId) { this.groupId = groupId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    @Override
    public String toString() {
        return "Group [ID=" + groupId + ", Name=" + name + "]";
    }
}