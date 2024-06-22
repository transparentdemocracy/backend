package be.tr.democracy.inmem;

import be.tr.democracy.query.PlenaryWriteModel;
import be.tr.democracy.vocabulary.plenary.Plenary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO write a test using Testcontainers
public class PlenaryRepository implements PlenaryWriteModel {

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PlenaryRepository(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upsert(Plenary plenary) {
        String sql = "INSERT INTO plenary (plenary_id, legislature, data, content)"
                     + " VALUES (:id, :legislature, to_jsonb(:data), :content)"
                     + " ON CONFLICT (plenary_id)"
                     + " DO UPDATE SET data = to_jsonb(:data), content = :content;";
        String data = null;
        try {
            // TODO convert to an internal model, don't couple to domain
            data = objectMapper.writeValueAsString(plenary);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't serialize plenary", e);
        }

        String content = toContent(plenary);

        SqlParameterSource namedParameters = new MapSqlParameterSource()
            .addValue("id", plenary.id())
            .addValue("legislature", plenary.legislature())
            .addValue("data", data)
            .addValue("content", content);
        jdbcTemplate.update(sql, namedParameters);
    }

    private String toContent(Plenary plenary) {
        // TODO include more information in plenary index?
        return plenary.motionsGroups().stream()
            .flatMap(it -> Stream.concat(Stream.of(it.titleNL(), it.titleNL()),
                it.motions().stream()
                    .flatMap(it2 -> Stream.of(it2.titleNL(), it2.titleFR()))))
            .collect(Collectors.joining(" "));
    }
}
