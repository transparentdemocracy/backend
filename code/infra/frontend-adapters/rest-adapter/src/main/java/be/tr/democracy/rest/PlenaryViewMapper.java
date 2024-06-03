package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.List;

public class PlenaryViewMapper {
    private PlenaryViewMapper() {
    }

    public static PageViewDTO<PlenaryViewDTO> mapViewPage(Page<Plenary> plenaryPage) {
        final var plenaryViewDTOS = plenaryPage.values().stream().map(PlenaryViewMapper::map).toList();
        return new PageViewDTO<PlenaryViewDTO>(plenaryPage.pageNr(),
                plenaryPage.pageSize(),
                plenaryPage.totalPages(),
                plenaryViewDTOS);
    }

    public static PlenaryViewDTO map(Plenary plenary) {
        return new PlenaryViewDTO(plenary.id(), plenary.title(), plenary.legislature(), plenary.plenaryDate(), plenary.pdfReportUrl(), plenary.htmlReportUrl(),
                mapMotions(plenary.motionsGroups()));
    }

    private static List<MotionGroupLinkViewDTO> mapMotions(List<MotionGroupLink> motionGroupLinks) {
        return motionGroupLinks.stream()
                .map(PlenaryViewMapper::mapMotionGroupLink).toList();
    }

    private static MotionGroupLinkViewDTO mapMotionGroupLink(MotionGroupLink motionGroupLink) {
        return new MotionGroupLinkViewDTO(motionGroupLink.motionGroupId(), motionGroupLink.titleNL(), motionGroupLink.titleFR(), mapMotionLinks(motionGroupLink.motions()));
    }

    private static List<MotionLinkViewDTO> mapMotionLinks(List<MotionLink> motions) {
        return motions.stream().map(PlenaryViewMapper::mapMotionLink).toList();
    }

    private static MotionLinkViewDTO mapMotionLink(MotionLink x) {
        return new MotionLinkViewDTO(x.motionId(), "" + x.agendaSeqNr(), "" + x.voteSeqNr(), x.titleNL(), x.titleFR());
    }
}
