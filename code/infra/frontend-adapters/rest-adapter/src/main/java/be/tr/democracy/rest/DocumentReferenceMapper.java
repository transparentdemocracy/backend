package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.DocumentReference;

enum DocumentReferenceMapper {
    INSTANCE;

    DocumentReferenceViewDTO mapDocumentReference(DocumentReference documentReference) {
        if (documentReference == null) {
            return null;
        }

        return new DocumentReferenceViewDTO(
                documentReference.spec(),
                documentReference.documentMainUrl(),
                documentReference.subDocuments().stream().map(it ->
                        new SubDocumentViewDTO(it.documentNr(), it.documentSubNr(), it.documentPdfUrl(), it.summaryNL(), it.summaryFR())
                ).toList()
        );
    }
}
