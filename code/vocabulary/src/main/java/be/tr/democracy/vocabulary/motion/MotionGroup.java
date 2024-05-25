package be.tr.democracy.vocabulary.motion;

import java.util.List;

//TODO validate the date format, use actual date
public record MotionGroup(String id,
                          String titleNL,
                          String titleFR,
                          List<Motion> motions,
                          String votingDate
) {

    public boolean containsMotion(String motionId) {
        return this.motions.stream().map(Motion::motionId).toList().contains(motionId);
    }
}
