package be.tr.democracy.vocabulary.plenary;

public record MotionLink(String motionId,
                         int agendaSeqNr,
                         int voteSeqNr,
                         String titleNL,
                         String titleFR
) {

    public MotionLink {
    }
}
