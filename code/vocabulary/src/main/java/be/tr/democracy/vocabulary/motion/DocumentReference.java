package be.tr.democracy.vocabulary.motion;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record DocumentReference(
        String spec,
        String documentMainUrl,
        List<SubDocument> subDocuments
) {

   public static DocumentReference NO_DOCUMENT = new DocumentReference("", "", Collections.emptyList());

    public DocumentReference {
        Objects.requireNonNull(spec);
        Objects.requireNonNull(subDocuments);
    }
}
