package be.tr.democracy.rest.document

import be.tr.democracy.api.UpsertDocumentSummary
import be.tr.democracy.vocabulary.motion.SubDocument
import jakarta.annotation.PreDestroy
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@CrossOrigin
@RestController
class DocumentController(private val upsertDocumentSummary: UpsertDocumentSummary) {

    val executorService: ExecutorService = Executors.newFixedThreadPool(20)

    @PreDestroy
    fun destroy() {
        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS)
    }

    @PostMapping("/document-summaries")
    fun upsert(@RequestBody request: DocumentSummaryDTO) {
        val parts = request.document_id.split("/")
        val docNr = parts[0].toInt(10)
        val docSubNr = parts[1].toInt(10)
        upsertDocumentSummary.upsert(
            SubDocument(
                // TODO hardcoded legislature nr
                "55/${docNr}/${docSubNr}",
                documentNr = docNr,
                documentSubNr = docSubNr,
                summaryNL = request.summary_nl,
                summaryFR = request.summary_fr,
            )
        )
    }

    @PostMapping("/document-summaries/bulk")
    fun upsertBulk(@RequestBody request: List<DocumentSummaryDTO>) {
        request.forEach {
            executorService.submit { upsert(it) }
        }
    }

    data class DocumentSummaryDTO(
        val document_id: String,
        val summary_nl: String,
        val summary_fr: String,
    )

}