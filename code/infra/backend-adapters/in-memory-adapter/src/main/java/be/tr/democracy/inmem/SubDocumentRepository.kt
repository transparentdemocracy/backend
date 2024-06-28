package be.tr.democracy.inmem

import be.tr.democracy.query.SubDocumentReadModel
import be.tr.democracy.query.SubDocumentWriteModel
import be.tr.democracy.vocabulary.motion.SubDocument
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val UPSERT_SUB_DOCUMENT: String = """
    INSERT INTO sub_document (document_id, document_nr, document_sub_nr, summary_nl, summary_fr)
    VALUES (:document_id, :document_nr, :document_sub_nr, :summary_nl, :summary_fr)
    ON CONFLICT (document_id)
    DO UPDATE SET summary_nl = :summary_nl, summary_fr = :summary_fr"""

class SubDocumentRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : SubDocumentWriteModel, SubDocumentReadModel {
    override fun upsert(subDocument: SubDocument) {
        jdbcTemplate.update(
            UPSERT_SUB_DOCUMENT,
            MapSqlParameterSource(
                mapOf(
                    "document_id" to subDocument.documentId,
                    "document_nr" to subDocument.documentNr,
                    "document_sub_nr" to subDocument.documentSubNr,
                    "summary_nl" to subDocument.summaryNL,
                    "summary_fr" to subDocument.summaryFR,
                )
            )
        )
    }

    // TODO class for SubDocumentId
    override fun findSubDocuments(subDocumentIds: List<String>): List<SubDocument> {
        if (subDocumentIds.isEmpty()) {
            return listOf();
        }

        return jdbcTemplate.query(
            """
            SELECT document_id, document_nr, document_sub_nr, summary_nl, summary_fr
            from sub_document
            where (document_id) in (:ids)
            """,
            MapSqlParameterSource(
                mapOf(
                    "ids" to subDocumentIds
                )
            )
        ) { rs, _ ->
            SubDocument(
                rs.getString("document_id"),
                rs.getInt("document_nr"),
                rs.getInt("document_sub_nr"),
                rs.getString("summary_nl"),
                rs.getString("summary_nl")
            )
        }
    }
}

