package exercise3;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class StateObjectModelProcessor {
    private final String filePath;

    public StateObjectModelProcessor(String filePath) {
        this.filePath = filePath;
    }

    private JsonArray fetchJsonArray() throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             JsonReader reader = Json.createReader(fis)) {
            return reader.readArray();
        }
    }

    public State findByAbbreviation(String abb) throws Exception {
        JsonArray jsonArray = fetchJsonArray();
        for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {
            if (obj.getString("Abbreviation").equalsIgnoreCase(abb)) {
                return parseState(obj);
            }
        }
        return null;
    }

    public List<State> findByYear(int year) throws Exception {
        List<State> result = new ArrayList<>();
        JsonArray jsonArray = fetchJsonArray();
        for (JsonObject obj : jsonArray.getValuesAs(JsonObject.class)) {
            if (obj.getInt("Statehood") < year) {
                result.add(parseState(obj));
            }
        }
        return result;
    }

    private State parseState(JsonObject obj) {
        return new State(
                obj.getInt("ID"),
                obj.getString("StateName"),
                obj.getString("Abbreviation"),
                obj.getString("Capital"),
                obj.getInt("Statehood")
        );
    }
}