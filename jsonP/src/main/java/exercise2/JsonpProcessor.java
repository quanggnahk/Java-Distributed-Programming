package exercise2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;

public class JsonpProcessor {
	public static void main(String[] args) throws Exception {
		Address address1 = new Address("21 2nd Street", "New York", "NY", 10021);
        List<PhoneNumber> phones1 = new ArrayList<>();
        phones1.add(new PhoneNumber("home", "212 555-1234"));
        phones1.add(new PhoneNumber("fax", "646 555-4567"));
        Person person1 = new Person("John", "Smith", 25, address1, phones1);

        Address address2 = new Address("123 Main St", "Los Angeles", "CA", 90001);
        List<PhoneNumber> phones2 = new ArrayList<>();
        phones2.add(new PhoneNumber("mobile", "310 555-7890"));
        Person person2 = new Person("Jane", "Doe", 28, address2, phones2);

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        String jsonpSingle = JsonpProcessor.convertObjectToJson(person1);
        System.out.println("--- CHUYỂN OBJECT SANG JSON ---");
        System.out.println(jsonpSingle);

        Person p1FromJsonp = JsonpProcessor.convertJsonToObject(jsonpSingle);
        System.out.println("\n--- PHỤC HỒI OBJECT TỪ CHUỖI JSON ---");
        System.out.println("Tên người dùng: " + p1FromJsonp.getFirstName() + " " + p1FromJsonp.getLastName());

        String jsonpList = JsonpProcessor.convertListToJson(personList);
        System.out.println("\n--- CHUYỂN LIST SANG JSON ARRAY ---");
        System.out.println(jsonpList);
        
        List<Person> listFromJsonp = JsonpProcessor.convertJsonToList(jsonpList);
        System.out.println("\n--- PHỤC HỒI LIST TỪ JSON ARRAY ---");
        System.out.println("Số lượng Person phục hồi được: " + listFromJsonp.size());
	}
	
	public static String convertObjectToJson(Person person) {
        JsonObjectBuilder addressBuilder = Json.createObjectBuilder()
                .add("streetAddress", person.getAddress().getStreetAddress())
                .add("city", person.getAddress().getCity())
                .add("state", person.getAddress().getState())
                .add("postalCode", person.getAddress().getPostalCode());

        JsonArrayBuilder phoneArrayBuilder = Json.createArrayBuilder();
        for (PhoneNumber phone : person.getPhoneNumbers()) {
            phoneArrayBuilder.add(Json.createObjectBuilder()
                    .add("type", phone.getType())
                    .add("number", phone.getNumber()));
        }

        JsonObject personJson = Json.createObjectBuilder()
                .add("firstName", person.getFirstName())
                .add("lastName", person.getLastName())
                .add("age", person.getAge())
                .add("address", addressBuilder)
                .add("phoneNumbers", phoneArrayBuilder)
                .build();

        StringWriter stringWriter = new StringWriter();
        try (JsonWriter writer = Json.createWriterFactory(Collections.singletonMap(jakarta.json.stream.JsonGenerator.PRETTY_PRINTING, true)).createWriter(stringWriter)) {
            writer.writeObject(personJson);
        }
        return stringWriter.toString();
    }

	public static Person convertJsonToObject(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject root = reader.readObject();
        return parsePersonObject(root);
    }

    public static Person convertFileToObject(String filePath) throws FileNotFoundException {
        JsonReader reader = Json.createReader(new FileInputStream(filePath));
        JsonObject root = reader.readObject();
        return parsePersonObject(root);
    }
    
    private static Person parsePersonObject(JsonObject root) {
    	JsonObject addrObj = root.getJsonObject("address");
        Address address = new Address(
                addrObj.getString("streetAddress"),
                addrObj.getString("city"),
                addrObj.getString("state"),
                addrObj.getInt("postalCode")
        );

        List<PhoneNumber> phones = new ArrayList<>();
        JsonArray phoneArray = root.getJsonArray("phoneNumbers");
        for (JsonObject phoneObj : phoneArray.getValuesAs(JsonObject.class)) {
            phones.add(new PhoneNumber(phoneObj.getString("type"), phoneObj.getString("number")));
        }

        return new Person(
                root.getString("firstName"),
                root.getString("lastName"),
                root.getInt("age"),
                address,
                phones
        );
    }

    public static String convertListToJson(List<Person> persons) {
        JsonArrayBuilder rootArrayBuilder = Json.createArrayBuilder();
        for (Person person : persons) {
            JsonReader reader = Json.createReader(new StringReader(convertObjectToJson(person)));
            rootArrayBuilder.add(reader.readObject());
        }
        return rootArrayBuilder.build().toString();
    }

    public static List<Person> convertJsonToList(String jsonArrayString) {
        JsonReader reader = Json.createReader(new StringReader(jsonArrayString));
        JsonArray jsonArray = reader.readArray();
        
        List<Person> persons = new ArrayList<>();
        for (JsonObject personObj : jsonArray.getValuesAs(JsonObject.class)) {
            persons.add(convertJsonToObject(personObj.toString()));
        }
        return persons;
    }
}