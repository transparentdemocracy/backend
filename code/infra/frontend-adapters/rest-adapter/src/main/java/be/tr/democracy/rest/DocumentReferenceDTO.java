package be.tr.democracy.rest;

import java.util.List;

public record DocumentReferenceDTO(
    String spec,
    String documentMainUrl,
    List<SubDocumentDTO> subDocuments
) {
}
