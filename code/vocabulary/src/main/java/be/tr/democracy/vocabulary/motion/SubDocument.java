package be.tr.democracy.vocabulary.motion;

public record SubDocument(
    Integer documentNr,
    Integer documentSubNr,
    String documentPdfUrl,
    String summaryNL,
    String summaryFR
) {
}
