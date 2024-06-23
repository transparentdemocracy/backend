package be.tr.democracy.query

import be.tr.democracy.vocabulary.motion.SubDocument

interface SubDocumentWriteModel {
    fun upsert(subDocument: SubDocument)
}
