package be.tr.democracy.inmem;

public record ProposalDTO(
        String id,
        String documents_reference,
        String title_nl,
        String title_fr
) {
}
