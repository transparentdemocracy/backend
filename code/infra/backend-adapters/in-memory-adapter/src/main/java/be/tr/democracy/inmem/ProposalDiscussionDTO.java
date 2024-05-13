package be.tr.democracy.inmem;

import java.util.List;

public record ProposalDiscussionDTO(
        String id,
        int plenary_agenda_item_number,
        String plenary_id,
        String description_nl,
        String description_fr,
        List<ProposalsDTO> proposals
) {
}
