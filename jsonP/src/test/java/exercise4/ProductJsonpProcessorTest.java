package exercise4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProductJsonpProcessorTest {

    private static ProductJsonpProcessor processor;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        processor = new ProductJsonpProcessor("json/exercise4.json");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @Test
    void testFindBySku() {
        Product product = processor.findBySku(43900);
        assertNotNull(product, "Sản phẩm mã 43900 phải tồn tại");
        assertEquals("Duracell - AAA Batteries (4-Pack)", product.getName(), "Tên sản phẩm không khớp");
        assertEquals("MN2400B4Z", product.getModel(), "Model sản phẩm không khớp");

        Product nullProduct = processor.findBySku(11111);
        assertNull(nullProduct, "Mã 11111 không tồn tại, phải trả về null");
    }

    @Test
    void testFindByPrice() {
        List<Product> products = processor.findByPrice(5.0, 6.0);
        assertNotNull(products, "Danh sách không được null");
        assertFalse(products.isEmpty(), "Phải có ít nhất 1 sản phẩm trong khoảng giá 5.0 - 6.0");
        
        for (Product p : products) {
            assertTrue(p.getPrice() >= 5.0 && p.getPrice() <= 6.0, 
                "Lỗi logic: Giá " + p.getPrice() + " không nằm trong khoảng yêu cầu");
        }

        List<Product> emptyList = processor.findByPrice(100.0, 200.0);
        assertTrue(emptyList.isEmpty(), "Danh sách trả về phải rỗng vì không có sản phẩm nào giá này");
    }

    @Test
    void testFindByManufacturer() {
        List<Product> products = processor.findByManufacturer("Duracell");
        
        assertNotNull(products, "Danh sách không được null");
        assertEquals(2, products.size(), "Dựa trên file JSON mẫu, phải có đúng 2 sản phẩm của Duracell");
    }
}