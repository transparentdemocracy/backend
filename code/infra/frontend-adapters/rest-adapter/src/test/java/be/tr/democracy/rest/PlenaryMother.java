package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.List;

public class PlenaryMother {

    static final Plenary FIRST_PLENARY = new Plenary("first_plenary_id", "50", "2024-01-02", "pdf", "html", buildMotionLinks());

    private PlenaryMother() {
    }

    private static List<MotionLink> buildMotionLinks() {
        return MotionsMother.DUMMY_MOTIONS.stream().map(x -> new MotionLink(x.motionId(), x.titleNL(), x.titleFR())).toList();
    }
}
