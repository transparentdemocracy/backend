package be.tr.democracy.rest;

import java.util.List;

public record PlenaryViewDTO(String id,
                             String legislature,
                             String date,
                             String pdfReportUrl,
                             String htmlReportUrl,
                             List<MotionLinkViewDTO> motions) {
}
