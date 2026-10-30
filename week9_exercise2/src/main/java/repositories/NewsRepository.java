package repositories;

import entities.News;
import entities.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface NewsRepository extends Remote {
    boolean addUser(User u) throws RemoteException;
    List<News> getNewsByTagsOrNewsCategories(String value) throws RemoteException;
    Map<User, Integer> getStatistics() throws RemoteException;
}