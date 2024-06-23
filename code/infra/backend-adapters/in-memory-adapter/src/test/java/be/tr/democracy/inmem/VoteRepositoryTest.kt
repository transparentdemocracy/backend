package be.tr.democracy.inmem

import be.tr.democracy.vocabulary.motion.VoteType.YES
import be.tr.democracy.vocabulary.vote.Vote
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class VoteRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var repository: VoteRepository

    @Test
    fun upsert() {
        val vote = Vote("55_123_10", "1234", YES)

        repository.upsert(vote)

        // TODO assert correctly persisted?
    }
}