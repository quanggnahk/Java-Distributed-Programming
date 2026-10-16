package exercise2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonProcessor {
	public static void main(String[] args) {
		try {
            Address address1 = new Address("21 2nd Street", "New York", "NY", 10021);
            List<PhoneNumber> phones1 = new ArrayList<>();
            phones1.add(new PhoneNumber("home", "212 555-1234"));
            phones1.add(new PhoneNumber("fax", "646 555-4567"));
            Person person1 = new Person("John", "Smith", 25, address1, phones1);

            System.out.println("========= CHUYỂN JAVA OBJECT -> JSON (JACKSON) =========");
            String jsonOutput = JacksonProcessor.convertObjectToJson(person1);
            System.out.println(jsonOutput);

            System.out.println("\n========= ĐỌC FILE JSON -> JAVA OBJECT (JACKSON) =========");
            String filePath = "json/exercise2.json"; 
            
            Person personFromFile = JacksonProcessor.convertFileToObject(filePath);
            System.out.println("Đã đọc thành công file: " + filePath);
            System.out.println("Tên: " + personFromFile.getFirstName() + " " + personFromFile.getLastName());
            System.out.println("Thành phố: " + personFromFile.getAddress().getCity());
            System.out.println("SĐT đầu tiên: " + personFromFile.getPhoneNumbers().get(0).getNumber());
            System.out.println("\n========= CHUYỂN TẬP OBJECT -> JSON ARRAY (JACKSON) =========");
            List<Person> personList = new ArrayList<>();
            personList.add(person1);
            
            Address address2 = new Address("123 Main St", "Los Angeles", "CA", 90001);
            List<PhoneNumber> phones2 = new ArrayList<>();
            phones2.add(new PhoneNumber("mobile", "310 555-7890"));
            personList.add(new Person("Jane", "Doe", 28, address2, phones2));

            String listJsonOutput = JacksonProcessor.convertListToJson(personList);
            System.out.println(listJsonOutput);

            System.out.println("\n========= ĐỌC JSON ARRAY -> TẬP OBJECT (JACKSON) =========");
            List<Person> listFromJson = JacksonProcessor.convertJsonToList(listJsonOutput);
            System.out.println("Đã phục hồi thành công danh sách gồm " + listFromJson.size() + " người.");

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
	}
	private static final ObjectMapper mapper = new ObjectMapper();
    public static String convertObjectToJson(Person person) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
    }
    public static Person convertFileToObject(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), Person.class);
    }
    public static String convertListToJson(List<Person> persons) throws JsonProcessingException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(persons);
    }
    public static List<Person> convertJsonToList(String jsonArrayString) throws IOException {
        return mapper.readValue(jsonArrayString, new com.fasterxml.jackson.core.type.TypeReference<List<Person>>() {});
    }
}