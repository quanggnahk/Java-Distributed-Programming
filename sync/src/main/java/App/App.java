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
		try (Neo4jDBConnection db = new Neo4jDBConnection()){
			PersonDao personDao = new PersonDaoImpl(db);

			Person person = new Person(
					"Kathy", 
					30, 
					LocalDate.of(2020, 10, 20),
					LocalDateTime.of(2026, 9, 19, 14, 54, 0)
			);
			
			personDao.addPerson(person);
				
			Person person2 = personDao.getPerson("Kathy");
			System.out.println("Get Person: " + person2);

			System.out.println("Person list:");
			List<Person> persons = personDao.getPersons();
			persons.forEach(System.out::println);

			System.out.println("Friend list:");
			List<Person> friends = personDao.getFriend("Nguyen Van A");
			friends.forEach(System.out::println);

			System.out.println("FriendCount:");
			Map<String, Integer> friendsCount = personDao.getFriendCount();
			friendsCount.forEach((name, count) -> System.out.println(name + ": " + count + " friend"));
			
			System.out.println("Update Person:");
			personDao.updatePerson("Kathy", 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
