package be.tr.democracy.vocabulary.motion;

import java.util.Objects;

public record SubDocument(
        Integer documentNr,
        Integer documentSubNr,
        String documentPdfUrl,
        String summaryNL,
        String summaryFR
) {
    public SubDocument {
        Objects.requireNonNull(documentNr);
        Objects.requireNonNull(documentSubNr);
        Objects.requireNonNull(documentPdfUrl);
        Objects.requireNonNull(summaryFR);
        Objects.requireNonNull(summaryNL);
    }
}
