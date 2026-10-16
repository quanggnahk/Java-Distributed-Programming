package exercise1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

public class JsonStreamGeneratorArrayDemo {
	public static void main(String[] args) throws FileNotFoundException{
		Map< String, Boolean> config = new HashMap< String, Boolean>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);
		OutputStream fos = new FileOutputStream("json/posts.json");
		JsonGeneratorFactory factory = Json.createGeneratorFactory(config);
		JsonGenerator jsonGenerator = factory.createGenerator(fos);
		jsonGenerator.writeStartArray(); // [
		for (int i = 1 ; i < 10 ; i++) {
			jsonGenerator.writeStartObject(); // {
		 // create a post
			Post post = new Post();
			post.setTitle("JSONP Demo " + i);
			post.setId(i);
			post.setDescription("Post about JSONP " + i);
			post.setContent("Markdown content here " + i);
		 
			String[] tags = {"Java " + i, "JSON "+ i};
		 // create some predefined tags
			post.setTags(tags);
		 
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
		 }
		 jsonGenerator.writeEnd(); // }
		 jsonGenerator.flush();
		 jsonGenerator.close();
	}
}