package be.tr.democracy.inmem

import be.tr.democracy.query.VoteReadModel
import be.tr.democracy.query.VoteWriteModel
import be.tr.democracy.vocabulary.motion.PartyVotes
import be.tr.democracy.vocabulary.motion.VoteCount
import be.tr.democracy.vocabulary.motion.VoteType
import be.tr.democracy.vocabulary.motion.Votes
import be.tr.democracy.vocabulary.plenary.Politician
import be.tr.democracy.vocabulary.vote.Vote
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val UPSERT_VOTE = """INSERT INTO vote(voting_id, politician_id, vote_type)
            VALUES (:voting_id, :politician_id, :vote_type)
            ON CONFLICT(voting_id, politician_id)
            DO UPDATE SET vote_type = :vote_type
            """

class VoteRepository(val jdbcTemplate: NamedParameterJdbcTemplate) : VoteWriteModel, VoteReadModel {

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

    // TODO persist enriched votes as well so we don't need to reconstruct this for every read?
    override fun getVoteCountsByVotingIds(votingIds: List<String>, politiciansById: Map<String, Politician>): Map<String, VoteCount> {
        if (votingIds.isEmpty()) {
            return emptyMap()
        }
        val votes = jdbcTemplate.query(
            """
            SELECT voting_id, politician_id, vote_type
            FROM vote
            WHERE voting_id
            IN (:votingIds)
        """,
            MapSqlParameterSource(mapOf("votingIds" to votingIds))
        ) { rs, _ ->
            Vote(rs.getString("voting_id"), rs.getString("politician_id"), VoteType.valueOf(rs.getString("vote_type")))
        }

        return votes.groupBy { it.votingId }.mapValues { entry ->
            val votesByType = entry.value.groupBy { it.voteType }.withDefault { listOf() }
            VoteCount(
                entry.key,
                createVotes(VoteType.YES, votesByType.getValue(VoteType.YES), politiciansById),
                createVotes(VoteType.NO, votesByType.getValue(VoteType.NO), politiciansById),
                createVotes(VoteType.ABSTENTION, votesByType.getValue(VoteType.ABSTENTION), politiciansById)
            )
        }
    }

    private fun createVotes(voteType: VoteType, votes: List<Vote>, politiciansById: Map<String, Politician>): Votes {
        // TODO what if we don't know the politician. Simply count as 'unknown party' or require foreign key constraints?
        return Votes(voteType, votes.size, votes.groupBy { politiciansById[it.politicianId]!!.party }.map { e ->
            PartyVotes(e.key, e.value.size)
        })
    }

}