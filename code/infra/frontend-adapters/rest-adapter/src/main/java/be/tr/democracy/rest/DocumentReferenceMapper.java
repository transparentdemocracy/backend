package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.DocumentReference;

enum DocumentReferenceMapper {
    INSTANCE;

    DocumentReferenceViewDTO mapDocumentReference(DocumentReference documentReference) {
        if (documentReference == null) {
            return null;
        }

        return new DocumentReferenceViewDTO(
                documentReference.documentReference(),
                documentReference.documentMainUrl(),
                documentReference.subDocuments().stream().map(it ->
                        new SubDocumentViewDTO(it.getDocumentNr(), it.getDocumentSubNr(), it.getDocumentPdfURL(), it.getSummaryNL(), it.getSummaryFR())
                ).toList()
        );
    }
}
