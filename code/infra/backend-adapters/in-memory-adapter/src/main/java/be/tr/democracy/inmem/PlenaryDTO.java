package be.tr.democracy.inmem;

import java.util.List;

public record PlenaryDTO(String id,
                         int number,
                         int legislature,
                         //The date format is YYYY-MM-DD : "2021-09-23"
                         String date,
                         String pdf_report_url,
                         String html_report_url,
                         List<ProposalDiscussionDTO> proposal_discussions,
                         List<MotionGroupDTO> motion_groups

) {
}
