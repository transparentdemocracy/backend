package be.tr.democracy.vocabulary;

import static java.util.Objects.requireNonNull;

public record Motion(
        String motionId,
        String plenaryId,
        int numberInPlenary,
        String description,
        VoteCount voteCount
) {
    public Motion {
        requireNonNull(motionId, "motionId must not be null");
        requireNonNull(plenaryId, "plenaryId must not be null");
        requireNonNull(description, "description must not be null");
        requireNonNull(voteCount, "voteCount must not be null");
    }
}
