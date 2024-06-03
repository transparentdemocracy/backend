package be.tr.democracy.vocabulary.plenary;

import java.util.List;

public record Plenary(String id,
                      String legislature,
                      String plenaryDate,
                      String pdfReportUrl,
                      String htmlReportUrl,
                      List<MotionGroupLink> motionsGroups) {
}
