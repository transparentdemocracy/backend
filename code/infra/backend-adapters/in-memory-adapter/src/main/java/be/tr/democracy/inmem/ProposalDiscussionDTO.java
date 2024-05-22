package be.tr.democracy.inmem;

import java.util.List;

public record ProposalDiscussionDTO(
        String id,
        int plenary_agenda_item_number,
        String plenary_id,
        String description_nl,
        List<String> description_nl_tags,
        String description_fr,
        List<String> description_fr_tags,
        List<ProposalDTO> proposals
) {
}
