package be.tr.democracy.rest;

import java.util.List;

public record MotionGroupLinkViewDTO(String motionGroupId,
                                     String titleNL,
                                     String titleFR,
                                     List<MotionLinkViewDTO> motionLinks
) {
}
