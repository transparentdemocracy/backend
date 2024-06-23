package be.tr.democracy.inmem

import be.tr.democracy.query.PoliticianReadModel
import be.tr.democracy.query.PoliticianWriteModel
import be.tr.democracy.vocabulary.plenary.Politician
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PoliticianRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) :
    PoliticianReadModel, PoliticianWriteModel {

    override fun upsert(politician: Politician) {
        jdbcTemplate.update(
            """
            INSERT INTO politician (id, full_name, party)
            VALUES (:id, :full_name, :party)
            ON CONFLICT (id) DO UPDATE SET full_name = :full_name, party = :party
            """,
            MapSqlParameterSource(
                mapOf(
                    "id" to politician.id,
                    "full_name" to politician.fullName,
                    "party" to politician.party,
                )
            )
        )
    }

    override fun findAll(): List<Politician> {
        val FIND_ALL = """
            SELECT id, full_name, party
            FROM politician"""
        return jdbcTemplate.query(
            FIND_ALL,
            MapSqlParameterSource(mapOf<String, Any>())
        )
        { rs, _ ->
            Politician(rs.getString("id"), rs.getString("full_name"), rs.getString("party"))
        }
    }
}