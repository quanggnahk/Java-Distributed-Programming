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
    private static PersonDao personDao;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        dbConnection = new Neo4jDBConnection();
        personDao = new PersonDaoImpl(dbConnection);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }

    @Test
    void testAddPerson() {
        String testName = "ReactiveBob";
        Person person = new Person(testName, 30, LocalDate.of(2020, 1, 1), LocalDateTime.now());
        
        personDao.addPerson(person).block();
        Person fetchedPerson = personDao.getPerson(testName).block();

        assertNotNull(fetchedPerson, "Đối tượng trả về không được phép null");
        assertEquals(testName, fetchedPerson.getName(), "Tên trả về phải khớp");
        assertEquals(30, fetchedPerson.getAge());
    }

    @Test
    void testUpdatePerson() {
        String testName = "ReactiveBob"; 
        
        personDao.updatePerson(testName, 55).block();
        Person updatedPerson = personDao.getPerson(testName).block();

        assertNotNull(updatedPerson);
        assertEquals(55, updatedPerson.getAge(), "Tuổi phải được cập nhật thành 55");
    }

    @Test
    void testDeletePerson() {
        String dummyName = "DummyForReactiveDelete";
        Person dummy = new Person(dummyName, 20, LocalDate.now(), LocalDateTime.now());
        
        personDao.addPerson(dummy).block();
        assertNotNull(personDao.getPerson(dummyName).block());

        Boolean isDeleted = personDao.deletePerson(dummyName).block();
        assertTrue(isDeleted != null && isDeleted, "Hàm deletePerson phải trả về true");
        
        Person deletedPerson = personDao.getPerson(dummyName).block();
        assertNull(deletedPerson, "Đối tượng phải trả về null sau khi bị xóa");
    }

    @Test
    void testGetPerson() {
        Person notFound = personDao.getPerson("NonExistentUserXYZ").block();
        assertNull(notFound, "Phải trả về null nếu không tìm thấy Person");
    }

    @Test
    void testGetPersons() {
        List<Person> persons = personDao.getPersons().collectList().block();
        
        assertNotNull(persons, "Danh sách không được null");
        assertTrue(persons.size() > 0, "Danh sách phải có dữ liệu"); 
    }

    @Test
    void testGetFriends() {
        List<Person> friends = personDao.getFriends("Alice").collectList().block();
        assertNotNull(friends, "Danh sách không được trả về null");
    }

    @Test
    void testGetFriendCount() {
        Map<String, Integer> friendCounts = personDao.getFriendCount().block();
        
        assertNotNull(friendCounts, "Map kết quả không được trả về null");
        if (!friendCounts.isEmpty()) {
            String firstPerson = friendCounts.keySet().iterator().next();
            Integer count = friendCounts.get(firstPerson);
            
            assertNotNull(count);
            assertTrue(count >= 0);
        }
    }
}