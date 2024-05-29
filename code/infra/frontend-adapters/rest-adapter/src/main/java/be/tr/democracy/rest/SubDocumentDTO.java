package be.tr.democracy.rest;

public record SubDocumentDTO(
    Integer documentNr,
    Integer documentSubNr,
    String documentPdfUrl,
    String summaryNL,
    String summaryFR
) {
}
