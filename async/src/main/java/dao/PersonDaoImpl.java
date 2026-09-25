package dao;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.ResultCursor;

import App.Neo4jDBConnection;
import entities.Person;

public class PersonDaoImpl implements PersonDao {
    private final Neo4jDBConnection dbConnection;

    public PersonDaoImpl(Neo4jDBConnection neo4jDBConnection) {
        this.dbConnection = neo4jDBConnection;
    }

    @Override
    public CompletionStage<Void> addPerson(Person person) {
        String sql = "CREATE (:Person {name: $name, age: $age, birthday: $bday, createdAt: $created})";
        AsyncSession session = dbConnection.getSession();
        
        return session.writeTransactionAsync(tx -> tx.runAsync(sql, Values.parameters(
                "name", person.getName(), 
                "age", person.getAge(),
                "bday", person.getBirthday(),
                "created", person.getCreatedAt()
        )).thenCompose(ResultCursor::consumeAsync))
          .thenApply(summary -> (Void) null)
          .whenComplete((res, err) -> session.closeAsync());
    }

    @Override
    public CompletionStage<Void> updatePerson(String name, int age) {
        String sql = "MATCH (p:Person {name: $name}) SET p.age = $age";
        AsyncSession session = dbConnection.getSession();
        
        return session.writeTransactionAsync(tx -> tx.runAsync(sql, Values.parameters("name", name, "age", age))
                .thenCompose(ResultCursor::consumeAsync))
                .thenApply(summary -> (Void) null)
                .whenComplete((res, err) -> session.closeAsync());
    }

    @Override
    public CompletionStage<Boolean> deletePerson(String name) {
        String sql = "MATCH (p:Person {name: $name}) DETACH DELETE p";
        AsyncSession session = dbConnection.getSession();
        
        return session.writeTransactionAsync(tx -> tx.runAsync(sql, Values.parameters("name", name))
                .thenCompose(ResultCursor::consumeAsync))
                .thenApply(summary -> summary.counters().nodesDeleted() > 0)
                .whenComplete((res, err) -> session.closeAsync());
    }
    
    @Override
    public CompletionStage<List<Person>> getFriends(String name) {
        String sql = "MATCH (p:Person {name: $name})-[:FRIEND]->(friend) " +
                     "RETURN friend.name AS name, friend.age AS age, friend.birthday AS birthday, friend.createdAt AS createdAt";
        AsyncSession session = dbConnection.getSession();
        
        return session.readTransactionAsync(tx -> tx.runAsync(sql, Values.parameters("name", name))
                .thenCompose(cursor -> cursor.listAsync(record -> {
                    Person p = new Person();
                    p.setName(record.get("name").asString());
                    p.setAge(record.get("age").asInt());
                    
                    if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
                    if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
                    
                    return p;
                })))
                .whenComplete((res, err) -> session.closeAsync());
    }

    @Override
    public CompletionStage<Person> getPerson(String name) {
        String sql = "MATCH (p:Person {name:$name}) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
        AsyncSession session = dbConnection.getSession();
        
        return session.readTransactionAsync(tx -> tx.runAsync(sql, Values.parameters("name", name))
                .thenCompose(ResultCursor::nextAsync))
                .thenApply(record -> {
                    if (record == null) return null;
                    
                    Person p = new Person();
                    p.setName(record.get("name").asString());
                    p.setAge(record.get("age").asInt());
                    
                    if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
                    if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
                    
                    return p;
                })
                .whenComplete((res, err) -> session.closeAsync());
    }

    @Override
    public CompletionStage<List<Person>> getPersons() {
        String sql = "MATCH (p:Person) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
        AsyncSession session = dbConnection.getSession();
        
        return session.readTransactionAsync(tx -> tx.runAsync(sql)
                .thenCompose(cursor -> cursor.listAsync(record -> {
                    Person p = new Person();
                    p.setName(record.get("name").asString());
                    p.setAge(record.get("age").asInt());
                    
                    if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
                    if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
                    
                    return p;
                })))
                .whenComplete((res, err) -> session.closeAsync());
    }

    @Override
    public CompletionStage<Map<String, Integer>> getFriendCount() {
        String cypher = "MATCH (p:Person)-[:FRIEND]->(f:Person) RETURN p.name AS name, count(f) AS friendCount";
        AsyncSession session = dbConnection.getSession();
        
        return session.readTransactionAsync(tx -> tx.runAsync(cypher)
                .thenCompose(cursor -> cursor.listAsync(record -> 
                        new AbstractMap.SimpleEntry<>(record.get("name").asString(), record.get("friendCount").asInt())
                )))
                .thenApply(entries -> {
                    Map<String, Integer> map = new HashMap<>();
                    entries.forEach(e -> map.put(e.getKey(), e.getValue()));
                    return map;
                })
                .whenComplete((res, err) -> session.closeAsync());
    }
}