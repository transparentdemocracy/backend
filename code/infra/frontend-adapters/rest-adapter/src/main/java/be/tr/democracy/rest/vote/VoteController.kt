package be.tr.democracy.rest.vote

import be.tr.democracy.api.UpsertVote
import be.tr.democracy.vocabulary.motion.VoteType
import be.tr.democracy.vocabulary.vote.Vote
import jakarta.annotation.PreDestroy
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@CrossOrigin
@RestController
class VoteController(private val upsertVote: UpsertVote) {

    val executorService: ExecutorService = Executors.newFixedThreadPool(20)

    @PreDestroy
    fun destroy() {
        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)
    }

    @PostMapping("/votes", consumes = [MediaType.APPLICATION_JSON_VALUE])
    // TODO secure this endpoint
    fun upsert(@RequestBody vote: UpsertVoteDTO) {
        executorService.submit {
            upsertVote.upsert(
                Vote(
                    vote.voting_id,
                    vote.politician_id,
                    vote.vote_type
                )
            )
        }
    }

    @PostMapping("/votes/bulk", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun bulkUpsert(@RequestBody request: List<UpsertVoteDTO>) {
        request.forEach {
            executorService.submit { upsert(it) }
        }
    }


    data class UpsertVoteDTO(
        val voting_id: String,
        val politician_id: String,
        val vote_type: VoteType,
    )
}