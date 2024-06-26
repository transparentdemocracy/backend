package be.tr.democracy.inmem;

import static org.assertj.core.api.Assertions.assertThat;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

class PlenaryRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    PlenaryRepository repository;

    @Test
    void upsert() {
        repository.upsert(new Plenary(
            "55_123",
            123,
            "123 (L55)",
            "55",
            "2024-06-21",
            List.of()));
    }

    @Test
    void find_initiallyEmpty() {
        repository.find("blub", new PageRequest(1, 10));
    }

    @Test
    void find_returnsPlenary() {
        Plenary plenary = new Plenary("55_123",
            123,
            "123 (L55)",
            "55",
            "2024-06-21",
            List.of(new MotionGroupLink(
                "55_123_xyz",
                "1",
                "Wetsvoorstel over het klimaat",
                "Projet de loi climat",
                null,
                List.of()
            )));
        repository.upsert(plenary);

        Page<Plenary> actual = repository.find("klimaat", new PageRequest(1, 10));

        assertThat(actual.values())
            .singleElement()
            .isEqualTo(plenary);
    }
}