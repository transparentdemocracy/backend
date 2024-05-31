package be.tr.democracy.rest;

public record MotionLinkViewDTO(String motionId,
                                String agendaSeqNr,
                                String voteSeqNr,
                                String titleNL,
                                String titleFR
) {
}
