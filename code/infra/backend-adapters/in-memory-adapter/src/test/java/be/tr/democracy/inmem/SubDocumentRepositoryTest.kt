package be.tr.democracy.inmem

import be.tr.democracy.vocabulary.motion.SubDocument
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SubDocumentRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var subDocumentRepository: SubDocumentRepository

    @Test
    fun upsert() {
        subDocumentRepository.upsert(
            SubDocument(
                "55/5000/6",
                5000,
                6,
                "summary in dutch",
                "summary in french"
            )
        )
    }
}