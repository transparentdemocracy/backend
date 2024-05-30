package be.tr.democracy.inmem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class JSONDataFileLoader {

    private final Logger logger = LoggerFactory.getLogger(JSONDataFileLoader.class);
    private final Clock clock = Clock.systemDefaultZone();

    public List<PlenaryDTO> loadPlenaries(String fileName) {
        return loadResourceAsStream(fileName, PlenaryDTO[].class);
    }

    public List<VoteDTO> loadVotes(String fileName) {
        return loadResourceAsStream(fileName, VoteDTO[].class);
    }

    public Map<String, PoliticianDTO> loadPolitician(String fileName) {
        final var politicianDTOS = loadResourceAsStream(fileName, PoliticianDTO[].class);
        return politicianDTOS.stream()
                .collect(toMap(PoliticianDTO::id, politicianDTO -> politicianDTO));
    }

    public List<SummaryDTO> loadSummaries(String summariesFileName) {
        return loadResourceAsStream(summariesFileName, SummaryDTO[].class);
    }

    private <T> List<T> loadResourceAsStream(String fileName, Class<T[]> aClass) {
        try {
            final var stream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (stream == null) logger.error("Unable to load the file {}", fileName);
            final var inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            final var objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(inputStreamReader, aClass));

        } catch (Throwable e) {
            logger.error("Unable to load {} from [{}]", aClass.getSimpleName(), fileName, e);
            return List.of();
        }
    }
}
