package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

enum PlenaryMapper {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(PlenaryMapper.class);

    List<Plenary> mapThem(List<PlenaryDTO> plenaryDTOS) {
        return plenaryDTOS.stream().map(x -> {
            final var legislature = Integer.toString(x.legislature());
            return new Plenary(
                    x.id(),
                    x.number(),
                    x.number() + " (L" + legislature + ")",
                    legislature,
                    x.date(),
                    mapMotionGroups(x.motion_groups())
            );
        }).toList();
    }

    private List<MotionGroupLink> mapMotionGroups(List<MotionGroupDTO> motionGroupDTOS) {
        return motionGroupDTOS.stream().map(x -> {
            final var motions = mapMotionLinks(x.motions());
            final var titleNL = nonEmptyTitleNL(x, motions);
            final var titleFR = nonEmptyTitleFRL(x, motions);
            return new MotionGroupLink(x.id(), x.plenary_agenda_item_number(), titleNL, titleFR, x.documents_reference(), motions);
        }).toList();
    }

    private String nonEmptyTitleNL(MotionGroupDTO motionGroupDTO, List<MotionLink> motions) {
        final var title = motionGroupDTO.title_nl();
        if (title == null || title.isBlank()) {
            if (motions == null || motions.isEmpty()) {
                return "NONE";
            } else {
                return motions.getFirst().titleNL();
            }
        }
        return title;
    }

    private String nonEmptyTitleFRL(MotionGroupDTO motionGroupDTO, List<MotionLink> motions) {
        final var title = motionGroupDTO.title_fr();
        if (title == null || title.isBlank()) {
            if (motions == null || motions.isEmpty()) {
                return "NONE";
            } else {
                return motions.getFirst().titleFR();
            }
        }
        return title;
    }

    private List<MotionLink> mapMotionLinks(List<MotionDTO> motionGroupDTOS) {
        return motionGroupDTOS.stream().map(this::mapQ).toList();
    }


    private MotionLink mapQ(MotionDTO motionDTO) {
        final var id = motionDTO.id();
        try {
            final var split = id.split("_");
            final var last = split.length - 1;
            final var voteSeqNr = Integer.parseInt(split[last].substring(1)) + 1;
            final var agendaSeqNr = Integer.parseInt(split[last - 1]);
            return new MotionLink(
                id,
                agendaSeqNr,
                voteSeqNr,
                motionDTO.title_nl(),
                motionDTO.title_fr(),
                motionDTO.documents_reference(),
                motionDTO.voting_id(),
                motionDTO.cancelled());
        } catch (Throwable e) {
            logger.error("Error parsing the motion link, extracting the agendaSeqNr and voteSegNr", e);
            // TODO just throw and/or fix underlying problem?
            return new MotionLink(
                id,
                0,
                0,
                motionDTO.title_nl(),
                motionDTO.title_fr(),
                motionDTO.documents_reference(),
                motionDTO.voting_id(),
                motionDTO.cancelled());
        }
    }

}
