package be.tr.democracy.inmem

import be.tr.democracy.query.SubDocumentWriteModel
import be.tr.democracy.vocabulary.motion.SubDocument
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

private const val UPSERT_SUB_DOCUMENT: String = """
    INSERT INTO sub_document (document_nr, document_sub_nr, summary_nl, summary_fr)
    VALUES (:document_nr, :document_sub_nr, :summary_nl, :summary_fr)
    ON CONFLICT (document_nr, document_sub_nr)
    DO UPDATE SET summary_nl = :summary_nl, summary_fr = :summary_fr"""

class SubDocumentRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : SubDocumentWriteModel {
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
}

