package be.tr.democracy.rest;

public record MotionViewDTO(String title, String votingDate, ProposalViewDTO proposal, Boolean votingResult) {
}
