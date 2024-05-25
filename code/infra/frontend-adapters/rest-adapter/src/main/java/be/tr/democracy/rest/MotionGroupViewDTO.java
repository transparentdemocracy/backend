package be.tr.democracy.rest;

import java.util.List;

public record MotionGroupViewDTO(String id,
                                 String titleNL,
                                 String titleFR,
                                 List<MotionViewDTO> motions,
                                 String votingDate
) {
}
