package be.tr.democracy.vocabulary.plenary;

import java.util.List;

// TODO change order of argments to make more sense.
public record Plenary(String id,
                      Integer number,
                      String title,
                      String legislature,
                      String plenaryDate,
                      List<MotionGroupLink> motionsGroups) {

}
