package be.tr.democracy.inmem;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class JSONPlenariesLoader {

    private final Logger logger = LoggerFactory.getLogger(JSONPlenariesLoader.class);

    public List<PlenaryDTO> load(String fileName) {
        Objects.requireNonNull(fileName);
        try {
            final var file = loadResourceFile(fileName);
            return parsePlenaryJSON(file);
        } catch (Throwable e) {
            logger.error("Unable to load Plenaries from [{}]", fileName, e);
            return List.of();
        }
    }

    private static List<PlenaryDTO> parsePlenaryJSON(File file) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    private File loadResourceFile(String fileName) throws URISyntaxException {
        final var resource = getClass().getClassLoader().getResource(fileName);
        final Path path = Paths.get(requireNonNull(resource).toURI());
        return path.toFile();
    }
}
