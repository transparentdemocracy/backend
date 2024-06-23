package be.tr.democracy.query

import be.tr.democracy.api.UpsertDocumentSummary
import be.tr.democracy.vocabulary.motion.SubDocument

class UpsertDocumentSummaryCommand(private val subDocumentWriteModel: SubDocumentWriteModel) : UpsertDocumentSummary {
    override fun upsert(subDocument: SubDocument) {
        subDocumentWriteModel.upsert(subDocument)
    }
}
