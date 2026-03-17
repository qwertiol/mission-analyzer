import java.io.File;

public class ParserFactory {
    public static MissionParser getParser(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".json")) {
            return new JsonMissionParser();
        } else if (name.endsWith(".xml")) {
            return new XmlMissionParser();
        } else if (name.endsWith(".txt")) {
            return new TxtMissionParser();
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + file.getName());
        }
    }
}