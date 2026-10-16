package exercise3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class StateStreamingProcessorTest {

    private static StateStreamingProcessor streamProcessor;
    
    private static final String FILE_PATH = "json/exercise3.json";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        streamProcessor = new StateStreamingProcessor(FILE_PATH);
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    void testFindByAbbreviation() throws Exception {
        State state = streamProcessor.findByAbbreviation("AK");
        assertNotNull(state, "Bang có mã 'AK' phải tồn tại");
        assertEquals("Alaska", state.getStateName(), "Tên bang phải là Alaska");
        assertEquals("Juneau", state.getCapital(), "Thủ phủ của Alaska phải là Juneau");
        assertEquals(1959, state.getStatehood(), "Năm thành lập của Alaska phải là 1959");

        State nullState = streamProcessor.findByAbbreviation("VN");
        assertNull(nullState, "Mã 'VN' không tồn tại, kết quả phải trả về null");
    }

    @Test
    void testFindByYear() throws Exception {
        int targetYear = 1900; 
        List<State> states = streamProcessor.findByYear(targetYear);
        
        assertNotNull(states, "Danh sách trả về không được null");
        assertFalse(states.isEmpty(), "Phải tìm thấy dữ liệu trong file");
        
        for (State s : states) {
            assertTrue(s.getStatehood() < targetYear, 
                "Lỗi: Bang " + s.getStateName() + " có năm (" + s.getStatehood() + ") không nhỏ hơn " + targetYear);
        }
    }
}