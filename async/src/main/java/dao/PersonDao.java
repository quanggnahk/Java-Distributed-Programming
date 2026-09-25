package dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import entities.Person;

public interface PersonDao {
	CompletionStage<Void> addPerson(Person person);
	CompletionStage<Void> updatePerson(String name, int age);
	CompletionStage<Boolean> deletePerson(String name);
	CompletionStage<Person> getPerson(String name);
	CompletionStage<List<Person>> getPersons();
	CompletionStage<List<Person>> getFriends(String name);
	CompletionStage<Map<String, Integer>> getFriendCount();
}