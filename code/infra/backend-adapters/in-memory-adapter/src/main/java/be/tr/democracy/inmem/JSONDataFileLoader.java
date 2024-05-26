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
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class JSONDataFileLoader {

    private final Logger logger = LoggerFactory.getLogger(JSONDataFileLoader.class);
    private final Clock clock = Clock.systemDefaultZone();

    public List<PlenaryDTO> loadPlenaries(String fileName) {
        return loadFile(fileName, this::parsePlenaryJSON, PlenaryDTO.class);
    }

    public List<VoteDTO> loadVotes(String fileName) {
        return loadFile(fileName, this::parseVoteJSON, VoteDTO.class);
    }

    public Map<String, PoliticianDTO> loadPolitician(String fileName) {
        return loadFile(fileName, this::parsePoliticianJSON, PoliticianDTO.class).stream().collect(Collectors.toMap(PoliticianDTO::id, politicianDTO -> politicianDTO));
    }


    private <T> List<T> loadFile(String fileName, FileParser<T> f, Class<T> dataType) {
        final var start = clock.millis();

        final var result = parseJSOnFile(fileName, f, dataType);

        final var end = clock.millis();
        logger.info("Loading {} {} in {} milisec from {}", result.size(), dataType.getSimpleName(), end - start, fileName);

        return result;
    }


    private <T> List<T> parseJSOnFile(String fileName, FileParser<T> f, Class<T> dataType) {
        requireNonNull(fileName);
        try {
            final var file = loadResourceFile(fileName);
            return f.parse(file);
        } catch (Throwable e) {
            logger.error("Unable to load {} from [{}]", dataType.getSimpleName(), fileName, e);
            return List.of();
        }
    }


    private List<PlenaryDTO> parsePlenaryJSON(File file) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<List<PlenaryDTO>>() {
        });
    }

    private List<VoteDTO> parseVoteJSON(File file) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<List<VoteDTO>>() {
        });
    }

    private List<PoliticianDTO> parsePoliticianJSON(File file) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<List<PoliticianDTO>>() {
        });
    }

    private File loadResourceFile(String fileName) throws URISyntaxException {
        final var resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) logger.error("Unable to load the file {}", fileName);
        final Path path = Paths.get(requireNonNull(resource).toURI());
        return path.toFile();
    }

    @FunctionalInterface
    interface FileParser<T> {
        List<T> parse(File file) throws IOException;
    }


}
