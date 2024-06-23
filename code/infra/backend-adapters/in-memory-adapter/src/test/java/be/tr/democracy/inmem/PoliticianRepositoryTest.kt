package be.tr.democracy.inmem

import be.tr.democracy.vocabulary.plenary.Politician
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.test.junit5.annotation.FlywayTestExtension
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest
@FlywayTestExtension
@Testcontainers
@Rollback
class PoliticianRepositoryTest {

    companion object {
        @Container
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("td")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }

    @Autowired
    lateinit var repository: PoliticianRepository;

    @Test
    fun `upsert and find`() {
        val politician = Politician("1234", "Stan Ockers", "Vive Le Velo")

        repository.upsert(politician)

        assertThat(repository.findAll())
            .singleElement()
            .isEqualTo(politician)
    }
}