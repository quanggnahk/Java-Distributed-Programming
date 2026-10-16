package exercise3;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class StateStreamingProcessor {
    private final String filePath;

    public StateStreamingProcessor(String filePath) {
        this.filePath = filePath;
    }

    public State findByAbbreviation(String abb) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             JsonParser parser = Json.createParser(fis)) {
            
            State currentState = null;
            String currentKey = null;

            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        currentState = new State();
                        break;
                    case KEY_NAME:
                        currentKey = parser.getString();
                        break;
                    case VALUE_STRING:
                        if ("StateName".equals(currentKey)) currentState.setStateName(parser.getString());
                        else if ("Abbreviation".equals(currentKey)) currentState.setAbbreviation(parser.getString());
                        else if ("Capital".equals(currentKey)) currentState.setCapital(parser.getString());
                        break;
                    case VALUE_NUMBER:
                        if ("ID".equals(currentKey)) currentState.setId(parser.getInt());
                        else if ("Statehood".equals(currentKey)) currentState.setStatehood(parser.getInt());
                        break;
                    case END_OBJECT:
                        if (currentState != null && abb.equalsIgnoreCase(currentState.getAbbreviation())) {
                            return currentState; 
                        }
                        break;
                    default: break;
                }
            }
        }
        return null;
    }

    public List<State> findByYear(int year) throws Exception {
        List<State> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             JsonParser parser = Json.createParser(fis)) {

            State currentState = null;
            String currentKey = null;

            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        currentState = new State();
                        break;
                    case KEY_NAME:
                        currentKey = parser.getString();
                        break;
                    case VALUE_STRING:
                        if ("StateName".equals(currentKey)) currentState.setStateName(parser.getString());
                        else if ("Abbreviation".equals(currentKey)) currentState.setAbbreviation(parser.getString());
                        else if ("Capital".equals(currentKey)) currentState.setCapital(parser.getString());
                        break;
                    case VALUE_NUMBER:
                        if ("ID".equals(currentKey)) currentState.setId(parser.getInt());
                        else if ("Statehood".equals(currentKey)) currentState.setStatehood(parser.getInt());
                        break;
                    case END_OBJECT:
                        if (currentState != null && currentState.getStatehood() < year) {
                            result.add(currentState);
                        }
                        break;
                    default: break;
                }
            }
        }
        return result;
    }
}