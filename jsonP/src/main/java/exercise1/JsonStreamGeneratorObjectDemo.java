package exercise1;

import java.io.StringWriter;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;

public class JsonStreamGeneratorObjectDemo {
	public static void main(String[] args) {
		StringWriter writer = new StringWriter();
		JsonGenerator jsonGenerator = Json.createGenerator(writer);
		 // Create a post
		Post post = new Post();
		post.setTitle("JsonGenerator Demo");
		post.setId(1);
		post.setDescription("JsonGenerator Description");
		post.setContent("Markdown content here");
		String[] tags = {"Java", "JSON"};
		 // Create some predefined tags
		post.setTags(tags);
		jsonGenerator.writeStartObject(); // {
		 
		jsonGenerator.write("id", post.getId()); 
		jsonGenerator.write("title", post.getTitle());
		jsonGenerator.write("description", post.getDescription());
		jsonGenerator.write("content", post.getContent());
		jsonGenerator.writeStartArray("tags"); // [
		for (String tag: post.getTags()) {
			jsonGenerator.write(tag);
		}
		jsonGenerator.writeEnd(); // ] end of tags array
		 
		jsonGenerator.writeEnd(); // }
		 
		jsonGenerator.flush();
		jsonGenerator.close();

		System.out.println(writer.toString());
	}
}