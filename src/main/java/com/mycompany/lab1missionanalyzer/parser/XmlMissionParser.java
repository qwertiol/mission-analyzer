import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser() {
        xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mission parse(File file) throws IOException {
        return xmlMapper.readValue(file, Mission.class);
    }
}