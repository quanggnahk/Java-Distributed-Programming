package entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

@Embeddable
public class CommentId implements Serializable {
	private static final long serialVersionUID = 1L;
    private long userId;
    private long newsId;

    public CommentId() {}

    public CommentId(long userId, long newsId) {
        this.userId = userId;
        this.newsId = newsId;
    }

    // Getters, setters, equals, hashCode
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
    public long getNewsId() { return newsId; }
    public void setNewsId(long newsId) { this.newsId = newsId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentId commentId = (CommentId) o;
        return userId == commentId.userId && newsId == commentId.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, newsId);
    }
}