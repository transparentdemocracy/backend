package be.tr.democracy.vocabulary.motion;

import java.util.List;

public record MotionGroup(String id,
                          String plenaryId,
                          String plenaryAgendaItemNumber,
                          String titleNL,
                          String titleFR,
                          String documentsReference,
                          List<Motion> motions,
                          String voteDate
) {

    public boolean containsMotion(String motionId) {
        return this.motions.stream().map(Motion::getMotionId).toList().contains(motionId);
    }
}
