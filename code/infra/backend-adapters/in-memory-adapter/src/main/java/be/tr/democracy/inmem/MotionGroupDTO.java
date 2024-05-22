package be.tr.democracy.inmem;

import java.util.List;

public record MotionGroupDTO(
        String id,
        int plenary_agenda_item_number,
        String title_nl,
        String title_fr,
        String documents_reference,
        String proposal_discussion_id,
        List<MotionDTO> motions
) {
}
