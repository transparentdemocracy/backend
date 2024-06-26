package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.List;

public class PlenaryMother {

    static final Plenary FIRST_PLENARY = new Plenary(
        "50-1", 1, "first_plenary_id", "50", "2024-01-02", buildMotionLinks());

    private PlenaryMother() {
    }

    private static List<MotionGroupLink> buildMotionLinks() {
        return MotionsMother.DUMMY_MOTION_GROUPS.stream().map(y ->
            new MotionGroupLink(
                y.id(),
                "1",
                y.titleNL(),
                y.titleFR(),
                null,
                mapMotionLinks(y.motions()))).toList();
    }

    private static List<MotionLink> mapMotionLinks(List<Motion> dummyMotions) {
        return dummyMotions.stream()
            .map(PlenaryMother::mapMotionLink)
            .toList();
    }

    private static MotionLink mapMotionLink(Motion x) {
        return new MotionLink(
            x.getMotionId(),
            1,
            2,
            x.getTitleNL(),
            x.getTitleFR(),
            x.getNewDocumentReference().documentReference(),
            x.getVotingId(),
            x.getVoteCancelled());
    }
}
