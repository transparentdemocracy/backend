package be.tr.democracy.rest;

public record MotionViewDTO(String title, String votingDate, String description, Boolean votingResult) {
}
