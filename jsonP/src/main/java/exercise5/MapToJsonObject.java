package exercise5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

public class MapToJsonObject {
	private static Map<String, Object> createSampleData() {
		 Map<String, Object> map = new HashMap<>();
		 map.put("id", 1);
		 map.put("name", "John Smith");
		 map.put("age", 22);
		 map.put("email", "john.smith@gmail.com");
		 map.put("isRetired", false);
		 map.put("salary", 1000.0D);
		 // Nested object (address)
		 Map<String, Object> address = new HashMap<>();
		 address.put("street", "123 Main St");
		 address.put("city", "New York");
		 address.put("zip", "10001");
		 map.put("address", address);
		 // Nested array (phones)
		 List<String> phones = new ArrayList<>();
		 phones.add("123-456-7890");
		 phones.add("987-654-3210");
		 map.put("phones", phones);
		 return map;
	}
	private static JsonObjectBuilder toJsonObjectBuilder(Map<String, Object> map) {
		JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Integer) {
				jsonObjectBuilder.add(key, (Integer) value);
			}
			else if (value instanceof Double) {
				jsonObjectBuilder.add(key, (Double) value);
			}
			else if (value instanceof Long) {
				jsonObjectBuilder.add(key, (Long) value);
			}
			else if (value instanceof String) {
				jsonObjectBuilder.add(key, (String) value);
			}
			else if (value instanceof Boolean) {
				jsonObjectBuilder.add(key, (Boolean) value);
			}
			else if (value instanceof JsonObjectBuilder) {
				jsonObjectBuilder.add(key, ((JsonObjectBuilder) value).build());
			}
			else if (value instanceof JsonArrayBuilder) {
				jsonObjectBuilder.add(key, ((JsonArrayBuilder) value).build());
			}
			else if (value == null) {
				jsonObjectBuilder.addNull(key);
			}
			else {
				jsonObjectBuilder.add(key, value.toString());
			}
		}
		return jsonObjectBuilder;
	}
	public static void main(String[] args) {
		Map<String, Object> map = createSampleData();
		JsonObjectBuilder jsonObjectBuilder = toJsonObjectBuilder(map);
		System.out.println(jsonObjectBuilder.build());
	}
}