package be.tr.democracy.inmem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootApplication
class TestApplication {

    @Bean
    PlenaryRepository plenaryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PlenaryRepository(jdbcTemplate);
    }

    @Bean
    PoliticianRepository politicianRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PoliticianRepository(jdbcTemplate);
    }

    @Bean
    VoteRepository voteRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new VoteRepository(jdbcTemplate);
    }

    @Bean
    SubDocumentRepository subDocumentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new SubDocumentRepository(jdbcTemplate);
    }

}