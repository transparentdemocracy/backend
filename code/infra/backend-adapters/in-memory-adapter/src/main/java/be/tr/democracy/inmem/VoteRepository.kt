package be.tr.democracy.inmem

import be.tr.democracy.query.VoteWriteModel
import be.tr.democracy.vocabulary.vote.Vote
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val UPSERT_VOTE = """INSERT INTO vote(voting_id, politician_id, vote_type)
            VALUES (:voting_id, :politician_id, :vote_type)
            ON CONFLICT(voting_id, politician_id)
            DO UPDATE SET vote_type = :vote_type
            """

class VoteRepository(val jdbcTemplate: NamedParameterJdbcTemplate) : VoteWriteModel {

    override fun upsert(vote: Vote) {
        jdbcTemplate.update(
            UPSERT_VOTE,
            MapSqlParameterSource(
                mapOf(
                    "voting_id" to vote.votingId,
                    "politician_id" to vote.politicianId,
                    "vote_type" to vote.voteType.name
                )
            )
        )
    }
}