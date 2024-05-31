package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

enum PlenaryMapper {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(DataModelMapper.class);

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
        final var id = motionDTO.id();
        try {
            final var split = id.split("_");
            final var last = split.length - 1;
            final var voteSeqNr = Integer.parseInt(split[last].substring(1)) + 1;
            final var agendaSeqNr = Integer.parseInt(split[last - 1]);
            return new MotionLink(id, agendaSeqNr, voteSeqNr, motionDTO.title_nl(), motionDTO.title_fr());
        } catch (Throwable e) {
            logger.error("Error parsing the motion link, extracting the agendaSeqNr and voteSegNr", e);
            return new MotionLink(id, 0, 0, motionDTO.title_nl(), motionDTO.title_fr());
        }
    }

}
