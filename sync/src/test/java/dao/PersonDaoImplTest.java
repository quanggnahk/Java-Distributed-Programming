package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import App.Neo4jDBConnection;
import entities.Person;

class PersonDaoImplTest {
	private static Neo4jDBConnection dbConnection;
    private static PersonDao personDAO;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		dbConnection = new Neo4jDBConnection();
        personDAO = new PersonDaoImpl(dbConnection);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		if (dbConnection != null) {
            dbConnection.close();
        }
	}

	@Test
	void testAddPerson() {
		personDAO.addPerson(new Person(
				"Bob", 
				30, 
				LocalDate.of(2020, 1, 1), 
				LocalDateTime.of(2026, 10, 1, 10, 10, 0)
		));
		
		Person person = personDAO.getPerson("Bob");

        assertEquals("Bob", person.getName(), "Tên trả về phải là 'Bob'");
	}

	@Test
	void testUpdatePerson() {
		personDAO.updatePerson("Bob", 20);
		Person person = personDAO.getPerson("Bob");

        assertEquals(20, person.getAge(), "Tuổi phảilà 20");
	}

	@Test
	void testDeletePerson() {
		personDAO.addPerson(new Person(
				"Charlie", 
				25, 
				LocalDate.of(2022, 5, 10), 
				LocalDateTime.of(2026, 9, 24, 11, 0, 0)
		));
		
		assertNotNull(personDAO.getPerson("Charlie"), "Charlie phải tồn tại trước khi xóa");
		
		personDAO.deletePerson("Charlie");
		
		Person deletedPerson = personDAO.getPerson("Charlie");
		assertNull(deletedPerson, "Charlie phải bị xóa khỏi cơ sở dữ liệu");
	}

	@Test
	void testGetPerson() {
		Person person = personDAO.getPerson("Bob");

        assertNotNull(person, "Đối tượng tìm thấy không được phép null");
        assertEquals("Bob", person.getName(), "Tên trả về phải là 'Bob'");
	}

	@Test
	void testGetPersons() {
        List<Person> persons = personDAO.getPersons();
        
        assertNotNull(persons, "Danh sách không được null");
        assertTrue(persons.size() >= 2, "Danh sách phải có ít nhất 2 người");
        assertTrue(persons.stream().anyMatch(p -> p.getName().equals("Alice")));
        assertTrue(persons.stream().anyMatch(p -> p.getName().equals("Bob")));
	}

	@Test
	void testGetFriend() {
		List<Person> friends = personDAO.getFriend("Alice");
		
		assertNotNull(friends, "Danh sách bạn bè không được null");
	}

	@Test
	void testGetFriendCount() {
		Map<String, Integer> friendCounts = personDAO.getFriendCount();
		
		assertNotNull(friendCounts, "Map kết quả không được trả về null");
		
		if (!friendCounts.isEmpty()) {
			String firstPerson = friendCounts.keySet().iterator().next();
			Integer count = friendCounts.get(firstPerson);
			
			assertNotNull(count, "Số lượng bạn bè không được null");
			assertTrue(count >= 0, "Số lượng bạn bè phải >= 0");
		}
	}
}
