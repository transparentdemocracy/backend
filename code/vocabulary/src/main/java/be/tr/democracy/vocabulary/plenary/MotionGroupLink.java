package be.tr.democracy.vocabulary.plenary;

import java.util.List;

public record MotionGroupLink(String id,
                              String plenaryAgendaItemNumber,
                              String titleNL,
                              String titleFR,
                              String documentReference,
                              List<MotionLink> motions
) {
}
