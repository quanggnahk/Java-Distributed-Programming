package dao;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Values;
import org.neo4j.driver.reactive.RxSession;
import App.Neo4jDBConnection;
import entities.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonDaoImpl implements PersonDao {
    private final Neo4jDBConnection dbConnection;

    public PersonDaoImpl(Neo4jDBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private Person mapToPerson(Record record) {
        Person p = new Person();
        p.setName(record.get("name").asString());
        p.setAge(record.get("age").asInt());
        if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
        if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
        return p;
    }

    @Override
    public Mono<Void> addPerson(Person person) {
        String sql = "CREATE (:Person {name: $name, age: $age, birthday: $bday, createdAt: $created})";
        
        return Mono.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> Mono.fromDirect(session.writeTransaction(tx -> 
                tx.run(sql, Values.parameters(
                    "name", person.getName(),
                    "age", person.getAge(),
                    "bday", person.getBirthday(),
                    "created", person.getCreatedAt()
                )).consume()
            )).then(),
            RxSession::close
        );
    }

    @Override
    public Mono<Void> updatePerson(String name, int age) {
        String sql = "MATCH (p:Person {name: $name}) SET p.age = $age";
        return Mono.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> Mono.fromDirect(session.writeTransaction(tx -> 
                tx.run(sql, Values.parameters("name", name, "age", age)).consume()
            )).then(),
            RxSession::close
        );
    }

    @Override
    public Mono<Boolean> deletePerson(String name) {
        String sql = "MATCH (p:Person {name: $name}) DETACH DELETE p";
        return Mono.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> Mono.fromDirect(session.writeTransaction(tx -> 
                tx.run(sql, Values.parameters("name", name)).consume()
            )).map(summary -> summary.counters().nodesDeleted() > 0),
            RxSession::close
        );
    }

    @Override
    public Mono<Person> getPerson(String name) {
        String sql = "MATCH (p:Person {name: $name}) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
        return Mono.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> Mono.fromDirect(session.readTransaction(tx -> 
                Mono.fromDirect(tx.run(sql, Values.parameters("name", name)).records())
                    .map(this::mapToPerson)
            )),
            RxSession::close
        );
    }

    @Override
    public Flux<Person> getPersons() {
        String sql = "MATCH (p:Person) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
        return Flux.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> session.readTransaction(tx -> 
                Flux.from(tx.run(sql).records()).map(this::mapToPerson)
            ),
            RxSession::close
        );
    }

    @Override
    public Flux<Person> getFriends(String name) {
        String sql = "MATCH (p:Person {name: $name})-[:FRIEND]->(friend) " +
                     "RETURN friend.name AS name, friend.age AS age, friend.birthday AS birthday, friend.createdAt AS createdAt";
        return Flux.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> session.readTransaction(tx -> 
                Flux.from(tx.run(sql, Values.parameters("name", name)).records()).map(this::mapToPerson)
            ),
            RxSession::close
        );
    }

    @Override
    public Mono<Map<String, Integer>> getFriendCount() {
        String cypher = "MATCH (p:Person)-[:FRIEND]->(f:Person) RETURN p.name AS name, count(f) AS friendCount";
        return Mono.usingWhen(
            Mono.fromSupplier(dbConnection::getSession),
            session -> Mono.fromDirect(session.readTransaction(tx -> 
                Flux.from(tx.run(cypher).records())
                    .collectMap(
                        record -> record.get("name").asString(),
                        record -> record.get("friendCount").asInt(),
                        HashMap::new
                    )
            )),
            RxSession::close
        );
    }
}