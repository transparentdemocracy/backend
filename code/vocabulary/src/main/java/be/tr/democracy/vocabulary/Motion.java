package be.tr.democracy.vocabulary;

import static java.util.Objects.requireNonNull;

public record Motion(
        String motionId,
        String plenaryId,
        String date,
        int numberInPlenary,
        String description,
        VoteCount voteCount
) {
    public Motion {
        requireNonNull(motionId, "motionId must not be null");
        requireNonNull(plenaryId, "plenaryId must not be null");
        requireNonNull(description, "description must not be null");
        requireNonNull(voteCount, "voteCount must not be null");
        requireNonNull(date, "date must not be null");
    }

    private Motion(Builder builder) {
        this(
                builder.motionId,
                builder.plenaryId,
                builder.date,
                builder.numberInPlenary,
                builder.description,
                builder.voteCount);
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String motionId;
        private String plenaryId;
        private String date;
        private int numberInPlenary;
        private String description;
        private VoteCount voteCount;

        private Builder() {
        }

        public Builder withMotionId(String val) {
            motionId = val;
            return this;
        }

        public Builder withPlenaryId(String val) {
            plenaryId = val;
            return this;
        }

        public Builder withDate(String val) {
            date = val;
            return this;
        }

        public Builder withNumberInPlenary(int val) {
            numberInPlenary = val;
            return this;
        }

        public Builder withDescription(String val) {
            description = val;
            return this;
        }

        public Builder withVoteCount(VoteCount val) {
            voteCount = val;
            return this;
        }

        public Motion build() {
            return new Motion(this);
        }
    }
}
