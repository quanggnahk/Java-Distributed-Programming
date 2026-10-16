package exercise1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class DecodeJsonReadFileDemo {
	public static void main(String[] args) throws FileNotFoundException{
		InputStream jsonFile = new FileInputStream("json/employee.json");
		JsonReader reader = Json.createReader(jsonFile);
		JsonObject jsonObject = reader.readObject();
		Employee employee = new Employee(
			jsonObject.getJsonNumber("id").longValue(),
			jsonObject.getString("name"),
			jsonObject.getJsonNumber("salary").doubleValue()
		);
		System.out.println(employee);
	}
}