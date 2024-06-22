package be.tr.democracy.vocabulary.plenary;

import java.util.List;

public record Plenary(String id,
                      String title,
                      String legislature,
                      String plenaryDate,
                      String pdfReportUrl,
                      String htmlReportUrl,
                      List<MotionGroupLink> motionsGroups) {

    public Plenary {
    }
}
