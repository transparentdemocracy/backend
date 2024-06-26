package be.tr.democracy.rest;

import java.util.List;

public record PlenaryViewDTO(String id,
                             String title,
                             String legislature,
                             String date,
                             String pdfReportUrl,
                             String htmlReportUrl,
                             List<MotionGroupLinkViewDTO> motionGroups) {
}
