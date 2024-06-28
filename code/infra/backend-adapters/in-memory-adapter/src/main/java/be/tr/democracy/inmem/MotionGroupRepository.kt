package be.tr.democracy.inmem

import be.tr.democracy.query.MotionGroupReadModel
import be.tr.democracy.query.MotionGroupWriteModel
import be.tr.democracy.vocabulary.motion.DocumentReference
import be.tr.democracy.vocabulary.motion.Motion
import be.tr.democracy.vocabulary.motion.MotionGroup
import be.tr.democracy.vocabulary.motion.PartyVotes
import be.tr.democracy.vocabulary.motion.SubDocument
import be.tr.democracy.vocabulary.motion.VoteCount
import be.tr.democracy.vocabulary.motion.VoteType
import be.tr.democracy.vocabulary.motion.Votes
import be.tr.democracy.vocabulary.page.Page
import be.tr.democracy.vocabulary.page.PageRequest
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.postgresql.util.PGobject
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet

private const val UPSERT_MOTION_GROUP: String = """
    INSERT INTO motion_group (plenary_id, id, data, content)
    VALUES (:plenary_id, :id, :data, :content)
    ON CONFLICT (plenary_id, id)
    DO UPDATE SET plenary_id = :plenary_id, data = to_jsonb(:data), content = :content"""

private const val FIND_MOTION_GROUPS = """
    SELECT data
    FROM motion_group
    WHERE to_tsquery('english', :searchTerm) @@ to_tsvector('english', content)
    LIMIT :limit
    OFFSET :offset"""

class MotionGroupRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : MotionGroupReadModel, MotionGroupWriteModel {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .registerKotlinModule()

    override fun upsert(motionGroup: MotionGroup) {
        val data = toData(motionGroup)
        jdbcTemplate.update(
            UPSERT_MOTION_GROUP,
            MapSqlParameterSource(
                mapOf(
                    "plenary_id" to motionGroup.plenaryId,
                    "id" to motionGroup.id,
                    "data" to data,
                    "content" to motionGroup.toContent()
                )
            )
        )
    }

    private fun toData(motionGroup: MotionGroup): PGobject {
        try {
            return PGobject().apply {
                type = "json"
                value = objectMapper.writeValueAsString(motionGroup.toStorage())
            }
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Can't serialize motionGroup", e)
        }

    }

    override fun find(searchTerm: String, pageRequest: PageRequest): Page<MotionGroup> {
        val motionGroups = jdbcTemplate.query(
            FIND_MOTION_GROUPS,
            MapSqlParameterSource(
                mapOf(
                    "searchTerm" to searchTerm,
                    "limit" to pageRequest.pageSize,
                    "offset" to (pageRequest.pageNr - 1) * pageRequest.pageSize
                )
            )
        ) { rs: ResultSet, _: Int ->
            val dataJson = (rs.getObject("data") as PGobject).value
            objectMapper
                .readValue(dataJson, MotionGroupStorage::class.java)
                .toDomain()
        }

        // TODO CATALYST remove 'totalPages' or implement a count query
        return Page(pageRequest.pageNr, pageRequest.pageSize, pageRequest.pageSize + 5, motionGroups)
    }

    override fun getMotionGroup(motionId: String): MotionGroup? {
        TODO("Not yet implemented")
    }

}

private fun MotionGroup.toContent(): String? {
    return "tomato"
}

private fun MotionGroup.toStorage(): MotionGroupStorage {
    return MotionGroupStorage(
        id,
        plenaryId,
        plenaryAgendaItemNumber,
        titleNL,
        titleFR,
        documentsReference, // TODO make this an object in domain?
        motions.map { it.toStorage() },
        voteDate,
    )
}

private fun Motion.toStorage(): MotionGroupStorage.Motion {
    return MotionGroupStorage.Motion(
        motionId,
        titleNL,
        titleFR,
        newDocumentReference?.toStorage(), // TODO fix document references
        description,
        voteDate,
        voteCount.toStorage(),
        votingId,
        voteCancelled,
        plenaryId,
        sequenceNumberInPlenary,
    )
}

private fun DocumentReference.toStorage(): MotionGroupStorage.DocumentReference {
    return MotionGroupStorage.DocumentReference(
        documentReference,
        documentMainUrl,
        subDocuments.map { it.toStorage() }
    )
}

private fun SubDocument.toStorage(): MotionGroupStorage.SubDocument {
    return MotionGroupStorage.SubDocument(
        documentNr,
        documentSubNr,
        summaryNL,
        summaryFR,
    )
}

private fun VoteCount.toStorage(): MotionGroupStorage.VoteCount {
    return MotionGroupStorage.VoteCount(
        motionId,
        yesVotes.toStorage(),
        noVotes.toStorage(),
        abstention.toStorage(),
    )
}

private fun Votes.toStorage(): MotionGroupStorage.Votes {
    return MotionGroupStorage.Votes(
        voteType,
        nrOfVotes,
        partyVotes.map { MotionGroupStorage.PartyVotes(it.partyName, it.numberOfVotes) }
    )
}

private fun MotionGroupStorage.toDomain(): MotionGroup {
    return MotionGroup(
        id,
        plenaryId,
        plenaryAgendaItemNumber,
        titleNL,
        titleFR,
        documentsReference,
        motions.map { it.toDomain() },
        voteDate
    )
}

private fun MotionGroupStorage.Motion.toDomain(): Motion {
    return Motion(
        motionId,
        titleNL,
        titleFR,
        documentReference?.toDomain(),
        description,
        voteDate,
        voteCount.toDomain(),
        votingId,
        voteCancelled,
        plenaryId,
        sequenceNumberInPlenary
    )
}

private fun MotionGroupStorage.DocumentReference.toDomain() = DocumentReference(
    documentReference,
    documentMainUrl,
    subDocuments.map { it.toDomain() })

private fun MotionGroupStorage.SubDocument.toDomain(): SubDocument {
    return SubDocument(
        documentNr,
        documentSubNr,
        summaryNL ?: "", // TODO make summaries nullable in vocabulary?
        summaryFR ?: ""
    )
}

private fun MotionGroupStorage.VoteCount.toDomain(): VoteCount {
    return VoteCount(
        motionId,
        yesVotes.toDomain(),
        noVotes.toDomain(),
        abstention.toDomain()
    )
}

private fun MotionGroupStorage.Votes.toDomain(): Votes {
    return Votes(
        voteType,
        nrOfVotes,
        partyVotes.map { it.toDomain() }
    )
}

private fun MotionGroupStorage.PartyVotes.toDomain(): PartyVotes {
    return PartyVotes(
        partyName,
        nrOfVotes
    )
}

private data class MotionGroupStorage(
    val id: String,
    val plenaryId: String,
    val plenaryAgendaItemNumber: String,
    val titleNL: String,
    val titleFR: String,
    val documentsReference: String?,
    val motions: List<Motion>,
    val voteDate: String,
) {
    data class Motion(
        val motionId: String,
        val titleNL: String,
        val titleFR: String,
        val documentReference: DocumentReference?,
        val description: String,
        val voteDate: String,
        val voteCount: VoteCount,
        val votingId: String,
        val voteCancelled: Boolean,
        val plenaryId: String,
        val sequenceNumberInPlenary: Int,
    )

    data class DocumentReference(
        val documentReference: String,
        val documentMainUrl: String?,
        val subDocuments: List<SubDocument>,
    )

    data class SubDocument(
        val documentNr: Int,
        val documentSubNr: Int,
        val summaryNL: String?,
        val summaryFR: String?,
    )

    data class VoteCount(
        val motionId: String,
        val yesVotes: Votes,
        val noVotes: Votes,
        val abstention: Votes,
    )

    data class Votes(
        val voteType: VoteType,
        val nrOfVotes: Int,
        val partyVotes: List<PartyVotes>,
    )

    data class PartyVotes(
        val partyName: String,
        val nrOfVotes: Int,
    )
}
