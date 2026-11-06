package dao;

import entity.News;
import entity.User;
import java.util.List;
import java.util.Map;

public interface NewsDao {
    boolean addUser(User u);
    List<News> getNewsByTagsOrNewsCategories(String value);
    Map<User, Integer> getStatistics();
}