package exercise1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class JavaObjectToJsonDemoWriteFile {
	public static void main(String[] args) throws FileNotFoundException{
		Employee emp = new Employee(10001, "John Smith", 10000d);
		 
		 JsonObject jsonObject = Json.createObjectBuilder()
		 .add("id", emp.getId())
		 .add("name", emp.getName())
		 .add("salary", emp.getSalary())
		 .build();
		 
		 String filePath = "json/employee.json";
		 PrintWriter out = new PrintWriter(new FileOutputStream(filePath),true);
		 out.println(jsonObject.toString());
		 out.close();
	}
}