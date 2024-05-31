package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.SubDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DocumentReferenceParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataModelMapper.class);
    private static final Pattern DOCUMENT_REFERENCE_PATTERN = Pattern.compile("^(\\d{4})/(.*)");
    private static final Pattern NUMERIC = Pattern.compile("^(\\d+)");
    private static final Pattern NUMERIC_RANGE = Pattern.compile("^(\\d+)-(\\d+)");
    private final Map<String, String> summariesNL;
    private final Map<String, String> summariesFR;

    public DocumentReferenceParser(List<SummaryDTO> summaryDTOS) {
        this.summariesNL = summaryDTOS.stream()

                .filter(it -> it.summary_nl() != null)
                .collect(Collectors.toMap(
                        SummaryDTO::document_id,
                        SummaryDTO::summary_nl
                ));
        this.summariesFR = summaryDTOS.stream()
                .filter(it -> it.summary_fr() != null)
                .collect(Collectors.toMap(
                        SummaryDTO::document_id,
                        SummaryDTO::summary_fr
                ));
    }

    Optional<DocumentReference> parseDocumentReference(String documentReference) {
        if (documentReference == null || documentReference.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(parse(documentReference));
    }

    private static String subDocumentUrl(Integer documentNr, Integer subDocumentNr) {
        return "https://www.dekamer.be/FLWB/PDF/55/%04d/55K%04d%03d.pdf".formatted(documentNr, documentNr, subDocumentNr);
    }

    private static List<Integer> getSubDocumentNumbers(String subDocumentNrSpec) {
        final Matcher subMatcher = NUMERIC.matcher(subDocumentNrSpec);
        if (subMatcher.matches()) {
            return List.of(Integer.parseInt(subMatcher.group(1), 10));
        }
        final Matcher subRangeMatcher = NUMERIC_RANGE.matcher(subDocumentNrSpec);
        if (subRangeMatcher.matches()) {
            return parseSubRange(subDocumentNrSpec, subRangeMatcher);
        }
        return List.of();
    }

    private static List<Integer> parseSubRange(String subDocumentNrSpec, Matcher subRangeMatcher) {
        int rangeStart = Integer.parseInt(subRangeMatcher.group(1), 10);
        int rangeEnd = Integer.parseInt(subRangeMatcher.group(2), 10);
        if ((rangeEnd - rangeStart) > 100) {
            LOGGER.warn(
                    "Refusing to create a subdocument range with more than 100 documents while parsing subdocument range %s".formatted(subDocumentNrSpec));
            return List.of();
        }
        return IntStream.rangeClosed(rangeStart, rangeEnd).boxed().toList();
    }

    private static String mainDocumentUrl(Integer documentNr) {
        return "https://www.dekamer.be/kvvcr/showpage.cfm?section=/flwb&language=nl&cfm=/site/wwwcfm/flwb/flwbn.cfm?lang=N&legislat=55&dossierID=%04d"
                .formatted(documentNr);
    }

    private static DocumentReference parseNumericDocumentReference(String documentReference) {
        Matcher numMatcher = NUMERIC.matcher(documentReference);
        if (numMatcher.matches()) {
            return new DocumentReference(documentReference, mainDocumentUrl(Integer.parseInt(numMatcher.group(0))), List.of());
        } else {
            return new DocumentReference(documentReference, "", List.of());
        }
    }

    private List<SubDocument> mapSubDocuments(List<Integer> subDocumentNrs, Integer documentNr) {
        return subDocumentNrs.stream().map(it -> mapSubDocument(documentNr, it)).toList();
    }

    private SubDocument mapSubDocument(Integer documentNr, Integer it) {
        final var summaryNL = summariesNL.getOrDefault("%d/%d".formatted(documentNr, it), "");
        final var summaryFR = summariesFR.getOrDefault("%d/%d".formatted(documentNr, it), "");
        final var documentPdfUrl = subDocumentUrl(documentNr, it);
        return new SubDocument(documentNr, it, documentPdfUrl, summaryNL, summaryFR);
    }

    private DocumentReference parseDocumentReferencePattern(String documentReference, Matcher matcher) {
        final Integer documentNr = Integer.parseInt(matcher.group(1), 10);
        final String subDocumentNrSpec = matcher.group(2);
        final List<Integer> subDocumentNrs = getSubDocumentNumbers(subDocumentNrSpec);
        final var subDocuments = mapSubDocuments( subDocumentNrs, documentNr);
        return new DocumentReference(documentReference,
                mainDocumentUrl(documentNr),
                subDocuments);
    }

    private DocumentReference parse(String documentReference) {
        final Matcher matcher = DOCUMENT_REFERENCE_PATTERN.matcher(documentReference);
        if (matcher.matches()) {
            return parseDocumentReferencePattern(documentReference, matcher);
        } else {
            return parseNumericDocumentReference(documentReference);
        }
    }
}
