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
                .addValue("data", toJson(plenary))
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

    private fun toJson(plenary: Plenary): PGobject {
        try {
            return PGobject().apply {
                type = "json"
                value = objectMapper.writeValueAsString(plenary.toPersistence())
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
                        .flatMap { it2: MotionLink -> Stream.of(it2.titleNL, it2.titleFR) })
            }
            .collect(Collectors.joining(" "))
    }

}

fun Plenary.toPersistence(): PlenaryStorage {
    return PlenaryStorage(
        id,
        title,
        legislature,
        plenaryDate,
        pdfReportUrl,
        htmlReportUrl,
        motionsGroups.map { it.toPersistence() }
    )
}

fun MotionGroupLink.toPersistence(): PlenaryStorage.MotionGroup {
    return PlenaryStorage.MotionGroup(
        motionGroupId,
        titleNL,
        titleFR,
    )
}

fun PlenaryStorage.toDomain(): Plenary {
    return Plenary(
        id,
        title,
        legislature,
        plenaryDate,
        pdfReportUrl,
        htmlReportUrl,
        motionsGroups?.map { it.toDomain() } ?: listOf()
    )
}

fun PlenaryStorage.MotionGroup.toDomain(): MotionGroupLink {
    return MotionGroupLink(
        motionGroupId,
        titleNL,
        titleFR,
        listOf() // TODO: map motions
    )
}

data class PlenaryStorage(
    val id: String,
    val title: String?,
    val legislature: String?,
    val plenaryDate: String?,
    val pdfReportUrl: String?,
    val htmlReportUrl: String?,
    val motionsGroups: List<MotionGroup>?,
) {
    data class MotionGroup(
        val motionGroupId: String?,
        val titleNL: String?,
        val titleFR: String?,
        // TODO map motions
    )
}