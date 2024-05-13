package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;

public class MotionMapper {

    private MotionMapper() {
    }

    public static MotionViewDTO map(Motion x) {
        final var voteCount = x.voteCount();
        return new MotionViewDTO(
                x.titleNL(), x.titleFR(),
                voteCount.nrOfYesVotes(),
                voteCount.nrOfNoVotes(),
                voteCount.nrOfAbsentees(),
                x.date(),
                x.descriptionNL(),
                x.descriptionFR(),
                voteCount.votePassed());
    }
}
