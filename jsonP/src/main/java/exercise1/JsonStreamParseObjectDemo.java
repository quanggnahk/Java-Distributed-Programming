package exercise1;

import java.io.StringReader;


import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;

public class JsonStreamParseObjectDemo {
	public static void main(String[] args) throws Exception{
		final String result = "{\"name\":\"John\",\"isRetired\":false,\"age\":42,\"skills\":[\"Java SE\", \"Java EE\"]}";
		final JsonParser jsonParser = Json.createParser(new StringReader(result));
				 
		while (jsonParser.hasNext()) {
			Event event = jsonParser.next();
			switch (event) {
				case KEY_NAME:
					System.out.println(jsonParser.getString());
					break;
				case VALUE_STRING:
					System.out.println(jsonParser.getString());
					break;
				case END_ARRAY:break;
				case END_OBJECT: break;
				case START_ARRAY: break;
				case START_OBJECT: break;
				case VALUE_TRUE: 
					System.out.println(true);
					break;
				case VALUE_FALSE:
					System.out.println(false);
					break;
				case VALUE_NULL: break;
				case VALUE_NUMBER: 
					System.out.println(jsonParser.getInt());
					break; 
				default: break;
			}
		}
		jsonParser.close();
	}
}