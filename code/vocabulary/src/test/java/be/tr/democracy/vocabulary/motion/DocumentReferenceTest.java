package be.tr.democracy.vocabulary.motion;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.List;

// TODO we need separate contects for document reference, list of individual references and list of subdocuments with summaries
// Now we are mixing all of these concepts
class DocumentReferenceTest {

    @Test
    void parseDocumentReferenceInvalid() {
        DocumentReference actual = DocumentReference.parseDocumentReference("garbage123");

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new DocumentReference("garbage123", null, List.of()));
    }

    @Test
    void parseDocumentReferenceSingle() {
        DocumentReference actual = DocumentReference.parseDocumentReference("1234/5");

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new DocumentReference("1234/5", "http://TODO", List.of(
                new SubDocument("55/1234/5", 1234, 5, null, null)
            )));
    }

    @Test
    void parseDocumentReferenceRange() {
        DocumentReference actual = DocumentReference.parseDocumentReference("1234/5-10");

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new DocumentReference("1234/5-10", "http://TODO", List.of(
                new SubDocument("55/1234/5", 1234, 5, null, null),
                new SubDocument("55/1234/6", 1234, 6, null, null),
                new SubDocument("55/1234/7", 1234, 7, null, null),
                new SubDocument("55/1234/8", 1234, 8, null, null),
                new SubDocument("55/1234/9", 1234, 9, null, null),
                new SubDocument("55/1234/10", 1234, 10, null, null)
            )));
    }

    @Test
    void parseDocumentReferenceHugeRange()  {
        DocumentReference actual = DocumentReference.parseDocumentReference("1234/100-201");

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(new DocumentReference("1234/100-201", "http://TODO", List.of()));
    }
}