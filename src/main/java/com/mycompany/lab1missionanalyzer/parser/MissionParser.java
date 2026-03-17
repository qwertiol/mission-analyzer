import java.io.File;
import java.io.IOException;

public interface MissionParser {
    Mission parse(File file) throws IOException;
}