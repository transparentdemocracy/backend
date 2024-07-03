package be.tr.democracy.inmem

import be.tr.democracy.vocabulary.motion.DocumentReference
import be.tr.democracy.vocabulary.motion.Motion
import be.tr.democracy.vocabulary.motion.MotionGroup
import be.tr.democracy.vocabulary.motion.PartyVotes
import be.tr.democracy.vocabulary.motion.SubDocument
import be.tr.democracy.vocabulary.motion.VoteCount
import be.tr.democracy.vocabulary.motion.VoteType
import be.tr.democracy.vocabulary.motion.VoteType.ABSTENTION
import be.tr.democracy.vocabulary.motion.VoteType.NO
import be.tr.democracy.vocabulary.motion.VoteType.YES
import be.tr.democracy.vocabulary.motion.Votes
import be.tr.democracy.vocabulary.page.PageRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

// TODO test is broken yo, fix this
@Disabled
class MotionGroupRepositoryTest: AbstractRepositoryTest() {
    @Autowired
    private lateinit var repository: MotionGroupRepository

    @Test
    fun `upsert and find`() {
        repository.upsert(motionGroup())

        val motionGroups = repository.find("tomato", PageRequest(1, 10))

        assertThat(motionGroups.values)
            .singleElement()
            .isEqualTo(motionGroup())
    }

    private fun motionGroup() = MotionGroup(
        "123_10",
        "123",
        "5",
        "dutch title",
        "french title",
        "1234/56",
        listOf(
            Motion(
                motionId = "motionId",
                titleNL = "dutch title",
                titleFR = "french title",
                description = "this is an example about tomato",
                voteDate = "2024-07-01",
                newDocumentReference = DocumentReference(
                    "1234/56", "http://example", listOf(
                        SubDocument("55/1234/56", 1234, 56, "dutch summary", "french summary")
                    )
                ),
                voteCount = VoteCount("motionId", votes(YES), votes(NO), votes(ABSTENTION)),
                votingId = "1234",
                voteCancelled = false,
                plenaryId = "55_123",
                sequenceNumberInPlenary = 5
            )
        ),
        "2024-07-01"
    )

    private fun votes(voteType: VoteType) = Votes(voteType, 1, listOf(partyVotes()))

    private fun partyVotes() = PartyVotes("party1", 1)
}