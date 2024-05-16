package be.tr.democracy.rest;

public record MotionViewDTO(String titleNL,
                            String titleFR,
                            VotesViewDTO yesVotes,
                            VotesViewDTO noVotes,
                            VotesViewDTO absVotes,
                            String votingDate,
                            String descriptionNL,
                            String descriptionFR,
                            Boolean votingResult) {
}
