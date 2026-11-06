package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
    @EmbeddedId
    private CommentId id;

    @ManyToOne
    @MapsId("userId") // Liên kết thuộc tính userId trong CommentId với đối tượng User
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("newsId") // Liên kết thuộc tính newsId trong CommentId với đối tượng News
    @JoinColumn(name = "news_id")
    private News news;

    private String commentContent;
    private LocalDateTime date;

    public Comment() {
    }

    public Comment(User user, News news, String commentContent, LocalDateTime date) {
        this.user = user;
        this.news = news;
        this.id = new CommentId(user.getId(), news.getId());
        this.commentContent = commentContent;
        this.date = date;
    }

    // Getters and Setters
    public CommentId getId() { return id; }
    public void setId(CommentId id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public News getNews() { return news; }
    public void setNews(News news) { this.news = news; }
    
    public String getCommentContent() { return commentContent; }
    public void setCommentContent(String commentContent) { this.commentContent = commentContent; }
    
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}