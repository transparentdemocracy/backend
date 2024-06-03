package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.List;

public class PlenaryMother {

    static final Plenary FIRST_PLENARY = new Plenary("first_plenary_id", "50", "2024-01-02", "pdf", "html", buildMotionLinks());

    private PlenaryMother() {
    }

    private static List<MotionGroupLink> buildMotionLinks() {
        return MotionsMother.DUMMY_MOTION_GROUPS.stream().map(y -> new MotionGroupLink(y.id(),y.titleNL(),y.titleFR(),mapMotionLinks(y.motions()))).toList();
    }

    private static List<MotionLink> mapMotionLinks(List<Motion> dummyMotions) {
        return dummyMotions.stream().map(x -> mapMotionLink(x)).toList();
    }

    private static MotionLink mapMotionLink(Motion x) {
        return new MotionLink(x.motionId(), 1, 2, x.titleNL(), x.titleFR());
    }
}
