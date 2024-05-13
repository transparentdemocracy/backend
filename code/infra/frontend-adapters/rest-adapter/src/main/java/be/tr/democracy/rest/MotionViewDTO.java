package be.tr.democracy.rest;

//TODO add votes
public record MotionViewDTO(String titleNL,
                            String titleFR,
                            Integer nrOfYesVotes,
                            Integer nrOfNoVotes,
                            Integer nrOfAbsentVotes,
                            String votingDate,
                            String descriptionNL,
                            String descriptionFR,
                            Boolean votingResult) {
}
