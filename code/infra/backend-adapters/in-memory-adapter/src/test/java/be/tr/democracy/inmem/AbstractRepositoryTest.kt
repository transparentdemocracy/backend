package be.tr.democracy.inmem

import org.flywaydb.test.junit5.annotation.FlywayTestExtension
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.startupcheck.StartupCheckStrategy
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootTest
@FlywayTestExtension
@Testcontainers
@Rollback
abstract class AbstractRepositoryTest {
    companion object {
        @Container
        val postgres: UnstoppablePostgresContainer = UnstoppablePostgresContainer.instance


        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }
}