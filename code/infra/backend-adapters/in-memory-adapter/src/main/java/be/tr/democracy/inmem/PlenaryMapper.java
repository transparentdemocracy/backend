package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;

import java.util.Collection;
import java.util.List;

enum PlenaryMapper {
    INSTANCE;

    List<Plenary> mapThem(List<PlenaryDTO> plenaryDTOS) {
        return plenaryDTOS.stream().map(x -> new Plenary(
                x.id(),
                Integer.toString(x.legislature()),
                x.date(),
                x.pdf_report_url(),
                x.html_report_url(),
                mapMotionLinks(x.motion_groups())
        )).toList();
    }

    private List<MotionLink> mapMotionLinks(List<MotionGroupDTO> motionGroupDTOS) {
        return motionGroupDTOS.stream().map(this::map).flatMap(Collection::stream).toList();
    }

    private List<MotionLink> map(MotionGroupDTO motionGroupDTO) {
        return motionGroupDTO.motions().stream().map(this::mapQ).toList();
    }

    private MotionLink mapQ(MotionDTO motionDTO) {
        return new MotionLink(motionDTO.id(), motionDTO.title_nl(), motionDTO.title_fr());
    }

}
