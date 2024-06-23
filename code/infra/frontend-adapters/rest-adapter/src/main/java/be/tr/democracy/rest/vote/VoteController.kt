package be.tr.democracy.rest.vote

import be.tr.democracy.api.UpsertVote
import be.tr.democracy.vocabulary.motion.VoteType
import be.tr.democracy.vocabulary.vote.Vote
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class VoteController(private val upsertVote: UpsertVote) {

    @PostMapping("/votes", consumes = [MediaType.APPLICATION_JSON_VALUE])
    // TODO secure this endpoint
    fun upsert(@RequestBody vote: UpsertVoteDTO) {
        upsertVote.upsert(
            Vote(
                vote.voting_id,
                vote.politician_id,
                vote.vote_type
            )
        )
    }


    data class UpsertVoteDTO(
        val voting_id: String,
        val politician_id: String,
        val vote_type: VoteType,
    )
}