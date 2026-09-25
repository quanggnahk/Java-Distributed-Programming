package dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.summary.ResultSummary;

import App.Neo4jDBConnection;
import entities.Person;

public class PersonDaoImpl implements PersonDao{
	private final Neo4jDBConnection dbConnection;

	public PersonDaoImpl(Neo4jDBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	@Override
	public void addPerson(Person person) {
		try (Session session = dbConnection.getSession()){
			String sql = "CREATE (:Person {name: $name, age: $age, birthday: $bday, createdAt: $created})";
			session.writeTransaction(tx -> {
				tx.run(sql, Values.parameters(
						"name", person.getName(), 
						"age", person.getAge(),
						"bday", person.getBirthday(),
						"created", person.getCreatedAt()
				));
				return null;
			});
		}
	}

	@Override
	public void updatePerson(String name, int age) {
		try (Session session = dbConnection.getSession()) {
			String sql = "MATCH (p:Person {name: $name}) SET p.age = $age";
			session.writeTransaction(tx -> {
				tx.run(sql, Values.parameters("name", name, "age", age));
				return null;
			});
		}
	}

	@Override
	public boolean deletePerson(String name) {
		try (Session session = dbConnection.getSession()) {
			return session.writeTransaction(tx -> {
				String sql = "MATCH (p:Person {name: $name}) DETACH DELETE p";
				
				ResultSummary resultSummary = tx.run(sql, Values.parameters("name", name)).consume();
				
				return resultSummary.counters().nodesDeleted() > 0;
			});
		}
	}

	@Override
	public Person getPerson(String name) {
		try (Session session = dbConnection.getSession()){
			return session.readTransaction(tx -> {
				String sql = "MATCH (p:Person {name: $name}) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
				Result result = tx.run(sql, Values.parameters("name", name));
				
				if (result.hasNext()) {
					Record record = result.next();
					Person person = new Person();
					person.setName(record.get("name").asString());
					person.setAge(record.get("age").asInt());
					
					if (!record.get("birthday").isNull()) {
						person.setBirthday(record.get("birthday").asLocalDate());
					}
					if (!record.get("createdAt").isNull()) {
						person.setCreatedAt(record.get("createdAt").asLocalDateTime());
					}
					return person;
				}
				return null;
			});
		}
	}

	@Override
	public List<Person> getPersons() {
		try (Session session = dbConnection.getSession()){
			return session.readTransaction(tx -> {
				List<Person> persons = new ArrayList<>();
				
				String sql = "MATCH (p:Person) RETURN p.name AS name, p.age AS age, p.birthday AS birthday, p.createdAt AS createdAt";
				Result result = tx.run(sql);
				
				while (result.hasNext()) {
					Record record = result.next();
					Person p = new Person();
					p.setName(record.get("name").asString());
					p.setAge(record.get("age").asInt());
					
					if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
					if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
					
					persons.add(p);
				}
				return persons;
			});
		}
	}

	@Override
	public List<Person> getFriend(String name) {
		try (Session session = dbConnection.getSession()) {
			return session.readTransaction(tx -> {
				String sql = "MATCH (p:Person {name: $name})-[:FRIEND]->(friend) "
						+ "RETURN friend.name AS name, friend.age AS age, friend.birthday AS birthday, friend.createdAt AS createdAt";
				
				List<Person> persons = new ArrayList<>();
				Result result = tx.run(sql, Values.parameters("name", name));

				while (result.hasNext()) {
					Record record = result.next();
					Person p = new Person();
					p.setName(record.get("name").asString());
					p.setAge(record.get("age").asInt());
					
					if (!record.get("birthday").isNull()) p.setBirthday(record.get("birthday").asLocalDate());
					if (!record.get("createdAt").isNull()) p.setCreatedAt(record.get("createdAt").asLocalDateTime());
					
					persons.add(p);
				}
				return persons;
			});
		}
	}

	@Override
	public Map<String, Integer> getFriendCount() {
		Map<String, Integer> friendCounts = new LinkedHashMap<>();
		try (Session session = dbConnection.getSession()){
			String sql = "MATCH (p:Person)-[:FRIEND]->(f:Person) RETURN p.name AS name, count(f) AS friendCounts";
			
			session.readTransaction(tx -> {
				Result result = tx.run(sql);
				while (result.hasNext()) {
					Record record = result.next();
					String name = record.get("name").asString();
					int count = record.get("friendCounts").asInt();
					friendCounts.put(name, count);
				}
				return null;
			});
		}
		return friendCounts;
	}
}
