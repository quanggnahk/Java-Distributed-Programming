package App;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import dao.PersonDao;
import dao.PersonDaoImpl;
import entities.Person;

public class App {
    public static void main(String[] args) {
        try (Neo4jDBConnection db = new Neo4jDBConnection()) {
            PersonDao personDao = new PersonDaoImpl(db);

            Person person = new Person(
                    "Kathy", 
                    30, 
                    LocalDate.of(2020, 10, 20),
                    LocalDateTime.of(2026, 9, 19, 14, 54, 0)
            );
            
            System.out.println("Đang thêm Person 'Kathy'...");
            personDao.addPerson(person).block();
            System.out.println("Thêm thành công!");
                
            System.out.println("\nGet Person:");
            Person person2 = personDao.getPerson("Kathy").block();
            System.out.println("Result: " + person2);

            System.out.println("\nPerson list:");
            List<Person> persons = personDao.getPersons().collectList().block();
            if (persons != null) {
                persons.forEach(System.out::println);
            }

            System.out.println("\nFriend list (Nguyen Van A):");
            List<Person> friends = personDao.getFriends("Nguyen Van A").collectList().block();
            if (friends != null && !friends.isEmpty()) {
                friends.forEach(System.out::println);
            } else {
                System.out.println("Không tìm thấy bạn bè nào.");
            }

            System.out.println("\nFriendCount:");
            Map<String, Integer> friendsCount = personDao.getFriendCount().block();
            if (friendsCount != null) {
                friendsCount.forEach((name, count) -> System.out.println(name + ": " + count + " friend(s)"));
            }
            
            System.out.println("\nUpdate Person (Kathy -> age: 20)...");
            personDao.updatePerson("Kathy", 20).block();
            
            Person updatedPerson = personDao.getPerson("Kathy").block();
            System.out.println("Sau khi cập nhật: " + updatedPerson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}