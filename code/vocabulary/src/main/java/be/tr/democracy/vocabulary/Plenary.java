package be.tr.democracy.vocabulary;

import java.util.List;

public record Plenary(String id,
                      String legislature,
                      String date,
                      String pdfReportUrl,
                      String htmlReportUrl,
                      List<MotionLink> motions) {
}
