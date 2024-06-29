package be.tr.democracy.vocabulary.plenary;

import java.util.Objects;

public record MotionLink(String motionId,
                         Integer agendaSeqNr,
                         Integer voteSeqNr,
                         String titleNL,
                         String titleFR,
                         String documentsReference,
                         String votingId,
                         Boolean cancelled
) {

    public MotionLink {
        Objects.requireNonNull(motionId);
        Objects.requireNonNull(votingId);
        Objects.requireNonNull(cancelled);
    }
}
