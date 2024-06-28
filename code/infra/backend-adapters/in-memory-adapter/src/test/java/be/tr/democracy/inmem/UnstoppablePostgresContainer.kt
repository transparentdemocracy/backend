package be.tr.democracy.inmem

import org.testcontainers.containers.PostgreSQLContainer


class UnstoppablePostgresContainer private constructor(): PostgreSQLContainer<UnstoppablePostgresContainer>(IMAGE_VERSION) {
    override fun start() {
        super.start()
        System.setProperty("DB_URL", container!!.jdbcUrl)
        System.setProperty("DB_USERNAME", container!!.username)
        System.setProperty("DB_PASSWORD", container!!.password)
    }

    override fun stop() {
    }

    companion object {
        private const val IMAGE_VERSION = "postgres:16-alpine"
        private var container: UnstoppablePostgresContainer? = null

        val instance: UnstoppablePostgresContainer
            get() {
                if (container == null) {
                    container = UnstoppablePostgresContainer()
                        .withDatabaseName("td")
                }
                return container!!
            }
    }
}
