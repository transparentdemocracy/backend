package be.tr.democracy.inmem;

public record MotionDTO(
        String id,
        int sequence_number,
        String title_nl,
        String title_fr,
        String description,
        String documents_reference,
        String voting_id,
        String proposal_id,
        boolean cancelled
) {
}
