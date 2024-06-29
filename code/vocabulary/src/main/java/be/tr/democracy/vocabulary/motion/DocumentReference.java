package be.tr.democracy.vocabulary.motion;

import kotlin.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public record DocumentReference(
    String documentReference,
    String documentMainUrl,
    List<SubDocument> subDocuments
) {

    public static final Logger LOGGER = LoggerFactory.getLogger(DocumentReference.class);
    private static final Pattern VALID_REFERENCE_PATTERN = Pattern.compile("(\\d+)/(\\d+)(-\\d+)?");

    public static DocumentReference parseDocumentReference(String documentReference) {
        return parseDocumentReference(documentReference, Map.of());
    }

    public static DocumentReference parseDocumentReference(String documentReference, Map<Pair<Integer, Integer>, SubDocument> subDocumentsById) {
        if (documentReference == null) {
            return null;
        }
        Matcher matcher = VALID_REFERENCE_PATTERN.matcher(documentReference);
        if (!matcher.matches()) {
            return new DocumentReference(documentReference, null, List.of());
        }

        // TODO main url requires legislature number, so add it to `parse`
        int mainNr = Integer.parseInt(matcher.group(1));
        int subNr1 = Integer.parseInt(matcher.group(2));
        String subNr2 = matcher.group(3);

        String documentMainUrl = "http://TODO";
        if (subNr2 == null) {
            return new DocumentReference(documentReference, documentMainUrl, List.of(
                // TODO harcoded legislature
                new SubDocument("55/%d/%d".formatted(mainNr, subNr1), mainNr, subNr1, null, null)
            ));
        }

        int subNr2Int = Integer.parseInt(subNr2.substring(1));

        if (subNr1 >= subNr2Int) {
            LOGGER.info("subDocument numbers out of order, swapping order");
            int tmp = subNr1;
            subNr1 = subNr2Int;
            subNr2Int = tmp;
        }
        if (subNr2Int - subNr1 > 100) {
            LOGGER.warn("too many subdocuments, chickening out for performance reasons");
            return new DocumentReference(documentReference, documentMainUrl, List.of());
        }

        List<SubDocument> subDocuments = IntStream.rangeClosed(subNr1, subNr2Int)
            .mapToObj(i -> {
                var idPair = new Pair<>(mainNr, i);
                var subDoc = subDocumentsById.get(idPair);
                // TODO hardcoded legislature
                return subDoc != null ? subDoc : new SubDocument("55/%d/%d".formatted(mainNr, i), mainNr, i, null, null);
            })
            .toList();

        return new DocumentReference(documentReference, documentMainUrl, subDocuments);
    }

    public DocumentReference {
        Objects.requireNonNull(documentReference);
        Objects.requireNonNull(subDocuments);
    }
}
