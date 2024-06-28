package be.tr.democracy.inmem

import be.tr.democracy.query.SubDocumentReadModel
import be.tr.democracy.query.SubDocumentWriteModel
import be.tr.democracy.vocabulary.motion.SubDocument
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val UPSERT_SUB_DOCUMENT: String = """
    INSERT INTO sub_document (document_nr, document_sub_nr, summary_nl, summary_fr)
    VALUES (:document_nr, :document_sub_nr, :summary_nl, :summary_fr)
    ON CONFLICT (document_nr, document_sub_nr)
    DO UPDATE SET summary_nl = :summary_nl, summary_fr = :summary_fr"""

class SubDocumentRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : SubDocumentWriteModel, SubDocumentReadModel {
    override fun upsert(subDocument: SubDocument) {
        jdbcTemplate.update(
            UPSERT_SUB_DOCUMENT,
            MapSqlParameterSource(
                mapOf(
                    "document_nr" to subDocument.documentNr,
                    "document_sub_nr" to subDocument.documentSubNr,
                    "summary_nl" to subDocument.summaryNL,
                    "summary_fr" to subDocument.summaryFR,
                )
            )
        )
    }

    // TODO CATALYST class for SubDocumentId
    override fun findSubDocuments(subDocumentIds: MutableList<Pair<Int, Int>>): List<SubDocument> {
        return jdbcTemplate.query("""
            SELECT document_nr, document_sub_nr, summary_nl, summary_fr
            from sub_document
            where (document_nr, document_sub_nr) in (:ids, :sub_ids)
            """,
            MapSqlParameterSource(mapOf(
                "ids" to subDocumentIds.map { it.first },
                "subIds" to subDocumentIds.map { it.second }
            ))) { rs, _ ->
            SubDocument(
                rs.getInt("document_nr"),
                rs.getInt("document_sub_nr"),
                rs.getString("summary_nl"),
                rs.getString("summary_nl")
            )
        }
    }
}

