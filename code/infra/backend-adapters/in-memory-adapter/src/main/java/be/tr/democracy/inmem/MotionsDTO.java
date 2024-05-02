package be.tr.democracy.inmem;

public record MotionsDTO(
        String id,
        int number,
        String proposal_id,
        boolean cancelled
) {
}
