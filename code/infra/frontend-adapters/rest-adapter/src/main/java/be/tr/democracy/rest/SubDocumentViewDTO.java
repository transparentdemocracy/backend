package be.tr.democracy.rest;

public record SubDocumentViewDTO(
    Integer documentNr,
    Integer documentSubNr,
    String documentPdfUrl,
    String summaryNL,
    String summaryFR
) {
}
