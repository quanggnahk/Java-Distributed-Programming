package exercise1;

import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class JsonArrayToJavaObjectDemo {
	public static void main(String[] args) throws FileNotFoundException{
		String json = "[{\"id\":10001,\"name\":\"John Smith\",\"salary\":10000.0},"
					+ "{\"id\":10002,\"name\":\"Tom Cruise\",\"salary\":20000.0}]";
		JsonReader reader = Json.createReader(new StringReader(json));
		JsonArray jsonArray = reader.readArray();
		List<Employee> employees = new ArrayList<Employee>();
		for (JsonObject jsonObject:jsonArray.getValuesAs(JsonObject.class)) {
			long id = jsonObject.getJsonNumber("id").longValue();
			String name = jsonObject.getString("name"); 
			double sal = jsonObject.getJsonNumber("salary").doubleValue();
			employees.add(new Employee(id, name, sal));
		}
		System.out.println(employees);
	}
}