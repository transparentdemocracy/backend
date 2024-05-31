package be.tr.democracy.rest;

import java.util.List;

public record DocumentReferenceViewDTO(
    String spec,
    String documentMainUrl,
    List<SubDocumentViewDTO> subDocuments
) {
}
