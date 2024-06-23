package be.tr.democracy.rest.politician

import be.tr.democracy.api.UpsertPolitician
import be.tr.democracy.vocabulary.plenary.Politician
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class PoliticianController(private val upsertPolitician: UpsertPolitician) {

    @PostMapping("/politicians", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun upsert(@RequestBody politician: UpsertPoliticianDTO) {
        upsertPolitician.upsert(
            Politician(
                politician.id,
                politician.full_name,
                politician.party
            )
        )
    }


    data class UpsertPoliticianDTO(
        val id: String,
        val full_name: String,
        val party: String,
    )
}