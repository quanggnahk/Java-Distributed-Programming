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
        String testName = "AsyncBob";
        Person person = new Person(
                testName, 
                30, 
                LocalDate.of(2020, 1, 1), 
                LocalDateTime.of(2026, 10, 1, 10, 10, 0)
        );
        
        personDao.addPerson(person).toCompletableFuture().join();
        
        Person fetchedPerson = personDao.getPerson(testName).toCompletableFuture().join();

        assertNotNull(fetchedPerson, "Đối tượng trả về không được phép null");
        assertEquals(testName, fetchedPerson.getName(), "Tên trả về phải khớp");
        assertEquals(30, fetchedPerson.getAge(), "Tuổi phải được lưu chính xác");
    }

    @Test
    void testUpdatePerson() {
        String testName = "AsyncBob";
        
        personDao.updatePerson(testName, 45).toCompletableFuture().join();
        
        Person updatedPerson = personDao.getPerson(testName).toCompletableFuture().join();

        assertNotNull(updatedPerson);
        assertEquals(45, updatedPerson.getAge(), "Tuổi phải được cập nhật thành 45");
    }

    @Test
    void testDeletePerson() {
        String dummyName = "DummyForAsyncDelete";
        Person dummy = new Person(dummyName, 20, LocalDate.now(), LocalDateTime.now());
        
        personDao.addPerson(dummy).toCompletableFuture().join();
        assertNotNull(personDao.getPerson(dummyName).toCompletableFuture().join());

        boolean isDeleted = personDao.deletePerson(dummyName).toCompletableFuture().join();
        assertTrue(isDeleted, "Hàm deletePerson phải trả về true khi xóa thành công");
        
        Person deletedPerson = personDao.getPerson(dummyName).toCompletableFuture().join();
        assertNull(deletedPerson, "Đối tượng phải trả về null sau khi bị xóa");
    }

    @Test
    void testGetPerson() {
        Person notFound = personDao.getPerson("GhostName12345").toCompletableFuture().join();
        assertNull(notFound, "Phải trả về null nếu không tìm thấy Person trong đồ thị");
    }

    @Test
    void testGetPersons() {
        List<Person> persons = personDao.getPersons().toCompletableFuture().join();
        
        assertNotNull(persons, "Danh sách không được null");
        assertTrue(persons.size() > 0, "Danh sách phải có dữ liệu"); 
    }

    @Test
    void testGetFriend() {
        String testName = "Alice"; 
        List<Person> friends = personDao.getFriends(testName).toCompletableFuture().join();
        
        assertNotNull(friends, "Hàm getFriend không được trả về null, nếu không có bạn thì trả về List rỗng");
    }

    @Test
    void testGetFriendCount() {
        Map<String, Integer> friendCounts = personDao.getFriendCount().toCompletableFuture().join();
        
        assertNotNull(friendCounts, "Map kết quả không được trả về null");
        
        if (!friendCounts.isEmpty()) {
            String firstPerson = friendCounts.keySet().iterator().next();
            Integer count = friendCounts.get(firstPerson);
            
            assertNotNull(count, "Giá trị số lượng bạn bè không được null");
            assertTrue(count >= 0, "Số lượng bạn bè phải >= 0");
        }
    }
}