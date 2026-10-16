package exercise3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class StateObjectModelProcessorTest {

    private static StateObjectModelProcessor omProcessor;
    
    private static final String FILE_PATH = "json/exercise3.json";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        omProcessor = new StateObjectModelProcessor(FILE_PATH);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    void testFindByAbbreviation() throws Exception {
        State state = omProcessor.findByAbbreviation("AL");
        assertNotNull(state, "Bang có mã 'AL' phải tồn tại trong file");
        assertEquals("Alabama", state.getStateName(), "Tên bang phải là Alabama");
        assertEquals("Montgomery", state.getCapital(), "Thủ phủ của Alabama phải là Montgomery");

        State nullState = omProcessor.findByAbbreviation("XX");
        assertNull(nullState, "Mã 'XX' không tồn tại, kết quả phải trả về null");
    }

    @Test
    void testFindByYear() throws Exception {
        int targetYear = 1900;
        List<State> states = omProcessor.findByYear(targetYear);
        
        assertNotNull(states, "Danh sách trả về không được null");
        assertFalse(states.isEmpty(), "Phải tìm thấy dữ liệu trong file");
        
        for (State s : states) {
            assertTrue(s.getStatehood() < targetYear, 
                "Lỗi: Bang " + s.getStateName() + " có năm (" + s.getStatehood() + ") không nhỏ hơn " + targetYear);
        }
    }
}