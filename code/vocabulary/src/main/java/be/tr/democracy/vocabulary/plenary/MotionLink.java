package be.tr.democracy.vocabulary.plenary;

public record MotionLink(String motionId,
                         Integer agendaSeqNr,
                         Integer voteSeqNr,
                         String titleNL,
                         String titleFR,
                         String documentsReference,
                         String votingId,
                         Boolean cancelled
) {

}
