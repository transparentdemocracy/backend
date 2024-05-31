package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.List;

public class PlenaryMapper {
    private PlenaryMapper() {
    }

    public static PageViewDTO<PlenaryViewDTO> mapViewPage(Page<Plenary> plenaryPage) {
        final var plenaryViewDTOS = plenaryPage.values().stream().map(PlenaryMapper::map).toList();
        return new PageViewDTO<PlenaryViewDTO>(plenaryPage.pageNr(),
                plenaryPage.pageSize(),
                plenaryPage.totalPages(),
                plenaryViewDTOS);
    }

    public static PlenaryViewDTO map(Plenary plenary) {
        return new PlenaryViewDTO(plenary.id(), plenary.legislature(), plenary.plenaryDate(), plenary.pdfReportUrl(), plenary.htmlReportUrl(),
                mapMotions(plenary.motions()));
    }

    private static List<MotionLinkViewDTO> mapMotions(List<MotionLink> motions) {
        return motions.stream().map(PlenaryMapper::map).toList();
    }

    private static MotionLinkViewDTO map(MotionLink x) {
        return new MotionLinkViewDTO(x.motionId(), "" + x.agendaSeqNr(), "" + x.voteSeqNr(), x.titleNL(), x.titleFR());
    }
}
