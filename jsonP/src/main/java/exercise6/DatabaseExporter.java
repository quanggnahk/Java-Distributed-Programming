package exercise6;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import java.io.FileWriter;
import java.sql.*;
import java.util.*;

public class DatabaseExporter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> tables = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
            DatabaseMetaData metaData = conn.getMetaData();

            try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    tables.add(tableName);
                }
            }

            System.out.println("\n=== DANH SÁCH CÁC BẢNG TRONG CƠ SỞ DỮ LIỆU ===");
            for (int i = 0; i < tables.size(); i++) {
                System.out.println((i + 1) + ". " + tables.get(i));
            }

            System.out.print("\nNhập chính xác tên bảng bạn muốn export ra JSON: ");
            String selectedTable = scanner.nextLine().trim();

            if (!tables.contains(selectedTable)) {
                System.out.println("Tên bảng không tồn tại trong CSDL. Kết thúc chương trình.");
                return;
            }

            exportTableToJson(conn, selectedTable);

        } catch (Exception e) {
            System.err.println("Lỗi hệ thống: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void exportTableToJson(Connection conn, String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

            while (rs.next()) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rsMetaData.getColumnName(i);
                    Object value = rs.getObject(i);

                    if (value == null) {
                        jsonObjectBuilder.addNull(columnName);
                    } else if (value instanceof Number) {
                        if (value instanceof Integer || value instanceof Short || value instanceof Byte) {
                            jsonObjectBuilder.add(columnName, ((Number) value).intValue());
                        } else {
                            jsonObjectBuilder.add(columnName, ((Number) value).doubleValue()); // float, double, decimal
                        }
                    } else if (value instanceof Boolean) {
                        jsonObjectBuilder.add(columnName, (Boolean) value);
                    } else {
                        jsonObjectBuilder.add(columnName, value.toString());
                    }
                }
                jsonArrayBuilder.add(jsonObjectBuilder);
            }

            String outputFileName = tableName + "_export.json";
            
            Map<String, Object> config = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
            JsonWriterFactory writerFactory = Json.createWriterFactory(config);

            try (FileWriter fileWriter = new FileWriter(outputFileName);
                 JsonWriter jsonWriter = writerFactory.createWriter(fileWriter)) {
                
                jsonWriter.writeArray(jsonArrayBuilder.build());
                System.out.println("\n✅ Export thành công! Dữ liệu đã được lưu vào file: " + outputFileName);
                
            } catch (Exception e) {
                System.err.println("Lỗi khi ghi file JSON: " + e.getMessage());
            }
        }
    }
}