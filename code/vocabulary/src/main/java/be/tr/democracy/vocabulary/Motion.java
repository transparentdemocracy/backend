package be.tr.democracy.vocabulary;

import static java.util.Objects.requireNonNull;

//TODO add proposal title fr/nl and document reference
public record Motion(
        String motionId,
        String plenaryId,
        String date,
        int numberInPlenary,
        String titleNL,
        String titleFR,
        String documentReference,
        String descriptionNL,
        String descriptionFR,
        VoteCount voteCount
) {
    public Motion {
        requireNonNull(motionId, "motionId must not be null");
        requireNonNull(plenaryId, "plenaryId must not be null");
        requireNonNull(titleNL, "titleNL must not be null");
        requireNonNull(titleFR, "titleFR must not be null");
        requireNonNull(documentReference, "documentReference must not be null");
        requireNonNull(descriptionNL, "descriptionNL must not be null");
        requireNonNull(descriptionFR, "descriptionFR must not be null");
        requireNonNull(voteCount, "voteCount must not be null");
        requireNonNull(date, "date must not be null");
        if (numberInPlenary < 0) {
            throw new RuntimeException("numberInPlenary must be greater than 0");
        }
    }

    private Motion(Builder builder) {
        this(
                builder.motionId,
                builder.plenaryId,
                builder.date,
                builder.numberInPlenary,
                builder.titleNL,
                builder.titleFR,
                builder.documentReference,
                builder.descriptionNL,
                builder.descriptionFR,
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
        private String titleNL;
        private String titleFR;
        private String documentReference;
        private String descriptionNL;
        private String descriptionFR;
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

        public Builder withDescriptionNL(String val) {
            descriptionNL = mapText(val);
            return this;
        }

        public Builder withTitleNL(String val) {
            titleNL = mapText(val);
            return this;
        }

        public Builder withTitleFR(String val) {
            titleFR = mapText(val);
            return this;
        }

        public Builder withDocumentReference(String val) {
            documentReference = mapText(val);
            return this;
        }

        public Builder withDescriptionFR(String val) {
            descriptionFR = mapText(val);
            return this;
        }

        public Builder withVoteCount(VoteCount val) {
            voteCount = val;
            return this;
        }

        public Motion build() {
            return new Motion(this);
        }

        private String mapText(String val) {
            return val == null || val.isEmpty() ? "N/A" : val;
        }
    }
}
