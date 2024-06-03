package be.tr.democracy.vocabulary.plenary;

import java.util.List;

public record MotionGroupLink(String motionGroupId,
                              String titleNL,
                              String titleFR,
                              List<MotionLink> motions
) {
}
