package be.tr.democracy.rest;

public record MotionViewDTO(String id,
                            String titleNL,
                            String titleFR,
                            VotesViewDTO yesVotes,
                            VotesViewDTO noVotes,
                            VotesViewDTO absVotes,
                            DocumentReferenceViewDTO newDocumentReference,
                            String votingDate,
                            String descriptionNL,
                            String descriptionFR,
                            Boolean votingResult) {
}
