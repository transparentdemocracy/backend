package be.tr.democracy.inmem;

import static org.assertj.core.api.Assertions.assertThat;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.flywaydb.test.junit5.annotation.FlywayTestExtension;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest
@FlywayTestExtension
@Testcontainers
@Rollback
class PlenaryRepositoryTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
        .withDatabaseName("td");

    @Autowired
    PlenaryRepository plenaryRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void upsert() {
        plenaryRepository.upsert(new Plenary("55_123",
            "123 (L55)",
            "55",
            "2024-06-21",
            "https://www.dekamer.be/doc/PCRI/pdf/55/ip123.pdf",
            "https://www.dekamer.be/doc/PCRI/html/55/ip123x.html",
            List.of()));
    }

    @Test
    void find_initiallyEmpty() {
        plenaryRepository.find("blub", new PageRequest(1, 10));
    }

    @Test
    void find_returnsPlenary() {
        Plenary plenary = new Plenary("55_123",
            "123 (L55)",
            "55",
            "2024-06-21",
            "https://www.dekamer.be/doc/PCRI/pdf/55/ip123.pdf",
            "https://www.dekamer.be/doc/PCRI/html/55/ip123x.html",
            List.of(new MotionGroupLink(
                "55_123_xyz",
                "Wetsvoorstel over het klimaat",
                "Projet de loi climat",
                List.of()
            )));
        plenaryRepository.upsert(plenary);

        Page<Plenary> actual = plenaryRepository.find("klimaat", new PageRequest(1, 10));

        assertThat(actual.values())
            .singleElement()
            .isEqualTo(plenary);
    }
}