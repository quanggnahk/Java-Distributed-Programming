package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "OnlineCourse")
@PrimaryKeyJoinColumn(name = "CourseID")
public class OnlineCourse extends Course {

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "URL")
    private String url;

    public OnlineCourse() {}
}