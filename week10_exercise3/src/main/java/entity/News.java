package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "news")
public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chuyển từ long -> Long

    private String newsTitle;

    @Column(columnDefinition = "TEXT")
    private String newsContent;

    private LocalDateTime creationDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "news_categories", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "category")
    private Set<String> newsCategories = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "news_tags", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "news_votes", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "vote")
    private Set<Integer> votes = new HashSet<>();

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    private transient Set<Comment> comments = new HashSet<>();

    public News() {}

    public News(String newsTitle, String newsContent, LocalDateTime creationDate) {
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.creationDate = creationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNewsTitle() { return newsTitle; }
    public void setNewsTitle(String newsTitle) { this.newsTitle = newsTitle; }
    public String getNewsContent() { return newsContent; }
    public void setNewsContent(String newsContent) { this.newsContent = newsContent; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public Set<String> getNewsCategories() { return newsCategories; }
    public void setNewsCategories(Set<String> newsCategories) { this.newsCategories = newsCategories; }
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
    public Set<Integer> getVotes() { return votes; }
    public void setVotes(Set<Integer> votes) { this.votes = votes; }
    public Set<Comment> getComments() { return comments; }
    public void setComments(Set<Comment> comments) { this.comments = comments; }
}