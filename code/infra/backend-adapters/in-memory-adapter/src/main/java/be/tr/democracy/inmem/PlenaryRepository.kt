package be.tr.democracy.inmem

import be.tr.democracy.query.PlenariesReadModel
import be.tr.democracy.query.PlenaryWriteModel
import be.tr.democracy.vocabulary.page.Page
import be.tr.democracy.vocabulary.page.PageRequest
import be.tr.democracy.vocabulary.plenary.MotionGroupLink
import be.tr.democracy.vocabulary.plenary.MotionLink
import be.tr.democracy.vocabulary.plenary.Plenary
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.postgresql.util.PGobject
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet
import java.util.Optional
import java.util.stream.Collectors
import java.util.stream.Stream

private const val UPSERT_PLENARY: String = """
    INSERT INTO plenary (plenary_id, legislature, data, content)
    VALUES (:id, :legislature, :data, :content)
    ON CONFLICT (plenary_id)
    DO UPDATE SET data = :data, content = :content;"""

private const val FIND_PLENARY = """
    SELECT data
    FROM plenary
    WHERE to_tsquery('english', :searchTerm) @@ to_tsvector('english', content)
    LIMIT :limit
    OFFSET :offset"""

// TODO Not sure if we need to store MotionGroupLink separately
class PlenaryRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : PlenaryWriteModel, PlenariesReadModel {
    private val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerModule(Jdk8Module())
        .registerKotlinModule()

    override fun upsert(plenary: Plenary) {
        val content = toContent(plenary)

        jdbcTemplate.update(
            UPSERT_PLENARY, MapSqlParameterSource()
                .addValue("id", plenary.id)
                .addValue("legislature", plenary.legislature)
                .addValue("data", toJson(plenary.toPlenaryStorage()))
                .addValue("content", content)
        )
    }

    override fun find(searchTerm: String, pageRequest: PageRequest): Page<Plenary> {
        // TODO create index and verify it's being used
        val plenaries = jdbcTemplate.query(
            FIND_PLENARY,
            MapSqlParameterSource()
                .addValue("searchTerm", searchTerm)
                .addValue("limit", pageRequest.pageSize)
                .addValue("offset", (pageRequest.pageNr - 1) * pageRequest.pageSize)
        ) { rs: ResultSet, rowNum: Int ->
            val dataJson = (rs.getObject("data") as PGobject).value
            objectMapper
                .readValue(dataJson, PlenaryStorage::class.java)
                .toDomain()
        }

        // TODO Get rid of 'totalPages' or implement a count
        return Page(
            pageRequest.pageNr, pageRequest.pageSize, pageRequest.pageNr + 10,
            plenaries
        )
    }

    override fun getPlenary(plenary: String): Optional<Plenary> {
        throw UnsupportedOperationException("todo")
    }

    private fun toJson(obj: Any): PGobject {
        try {
            return PGobject().apply {
                type = "json"
                value = objectMapper.writeValueAsString(obj)
            }
        } catch (e: JsonProcessingException) {
            throw RuntimeException("Can't serialize plenary", e)
        }
    }

    private fun toContent(plenary: Plenary): String {
        return plenary.motionsGroups.stream()
            .flatMap { it: MotionGroupLink ->
                Stream.concat(
                    Stream.of(it.titleNL, it.titleFR),
                    it.motions.stream()
                        .flatMap { motionLink: MotionLink -> Stream.of(motionLink.titleNL, motionLink.titleFR) })
            }
            .collect(Collectors.joining(" "))
    }

}

fun Plenary.toPlenaryStorage(): PlenaryStorage {
    return PlenaryStorage(
        id,
        legislature,
        number,
        title,
        plenaryDate,
        motionsGroups.map { it.toPlenaryStorage() }
    )
}

fun MotionGroupLink.toPlenaryStorage(): PlenaryStorage.MotionGroupLink {
    return PlenaryStorage.MotionGroupLink(
        id,
        plenaryAgendaItemNumber,
        documentReference,
        titleNL,
        titleFR,
        motions.map {
            it.toPersistence()
        }
    )
}

fun MotionLink.toPersistence(): PlenaryStorage.Motion {
    return PlenaryStorage.Motion(
        id = motionId,
        agendaSeqNr = agendaSeqNr,
        voteSeqNr = voteSeqNr,
        title_nl = titleNL,
        title_fr = titleFR,
        documents_reference = documentsReference,
        voting_id = votingId,
        proposal_id = null,
        cancelled = cancelled,
    )
}

fun PlenaryStorage.toDomain(): Plenary {
    return Plenary(
        id,
        number,
        title,
        legislature,
        plenaryDate,
        motionsGroups.map { it.toDomain() }
    )
}

fun PlenaryStorage.MotionGroupLink.toDomain(): MotionGroupLink {
    return MotionGroupLink(
        motionGroupId,
        plenaryAgendaItemNumber,
        titleNL,
        titleFR,
        documentReference,
        motions.map {
            it.toDomain()
        }
    )
}

fun PlenaryStorage.Motion.toDomain() = MotionLink(
    id,
    agendaSeqNr,
    voteSeqNr,
    title_nl,
    title_fr,
    documents_reference,
    voting_id,
    cancelled
)

data class PlenaryStorage(
    val id: String,
    val legislature: String?,
    val number: Int?,
    val title: String?,
    val plenaryDate: String?,
    val motionsGroups: List<MotionGroupLink>,
) {
    data class MotionGroupLink(
        val motionGroupId: String?,
        val plenaryAgendaItemNumber: String?,
        val documentReference: String?,
        val titleNL: String?,
        val titleFR: String?,
        val motions: List<Motion>,
    )

    data class Motion(
        val id: String,
        val agendaSeqNr: Int,
        val voteSeqNr: Int,
        val title_nl: String,
        val title_fr: String,
        val documents_reference: String?,
        val voting_id: String,
        val proposal_id: String?,
        val cancelled: Boolean,
    )
}

