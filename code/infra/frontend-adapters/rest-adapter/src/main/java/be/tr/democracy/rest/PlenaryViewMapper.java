package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.Collection;
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
        return new PlenaryViewDTO(plenary.id(), plenary.legislature(), plenary.plenaryDate(), plenary.pdfReportUrl(), plenary.htmlReportUrl(),
                mapMotions(plenary.motionsGroups()));
    }

    private static List<MotionLinkViewDTO> mapMotions(List<MotionGroupLink> motionGroupLinks) {
        //TODO For now motion group are not displayed yet in the plenaries. But they should be.
        return motionGroupLinks.stream()
                .map(MotionGroupLink::motions)
                .flatMap(Collection::stream)
                .map(PlenaryViewMapper::mapMotionLink).toList();
    }

    private static MotionLinkViewDTO mapMotionLink(MotionLink x) {
        return new MotionLinkViewDTO(x.motionId(), "" + x.agendaSeqNr(), "" + x.voteSeqNr(), x.titleNL(), x.titleFR());
    }
}
