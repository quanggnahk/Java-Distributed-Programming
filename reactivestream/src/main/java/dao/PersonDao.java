package dao;

import java.util.Map;

import entities.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonDao {
    Mono<Void> addPerson(Person person);
    Mono<Void> updatePerson(String name, int age);
    Mono<Boolean> deletePerson(String name);
    
    Mono<Person> getPerson(String name);
    
    Flux<Person> getPersons();
    Flux<Person> getFriends(String name);
    
    public Mono<Map<String, Integer>> getFriendCount();
}