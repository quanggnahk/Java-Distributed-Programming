package dao;

import java.util.List;
import java.util.Map;

import entities.Person;

public interface PersonDao {
	void addPerson(Person person);
	void updatePerson(String name, int age);
	boolean deletePerson(String name);
	Person getPerson(String name);
	List<Person> getPersons();
	List<Person> getFriend(String name);
	Map<String, Integer> getFriendCount();
}