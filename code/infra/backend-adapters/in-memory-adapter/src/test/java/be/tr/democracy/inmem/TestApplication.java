package be.tr.democracy.inmem;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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

    @Bean
    MotionGroupRepository motionGroupRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new MotionGroupRepository(jdbcTemplate);
    }

    @Configuration
    public class HikariSetting{

        @Bean
        public HikariConfig config() {
            HikariConfig hikariConfig = new HikariConfig();

            // other setting

            hikariConfig.setMaximumPoolSize(1000);
            hikariConfig.addDataSourceProperty("socketTimeout", 5000);
            hikariConfig.setMaxLifetime(5000);

            return hikariConfig;
        }

    }
}