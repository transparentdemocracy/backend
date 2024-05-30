package be.tr.democracy.inmem;

public record SummaryDTO(
    String document_id,
    String summary_nl,
    String summary_fr
) {
}
