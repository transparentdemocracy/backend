package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.motion.VoteCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

class DataModelMapper {
    private final Logger logger = LoggerFactory.getLogger(DataModelMapper.class);
    private final VoteCountFactory voteCountFactory;
    private final List<VoteDTO> voteDTOS;
    private final List<PlenaryDTO> plenaryDTOS;
    private final DocumentReferenceParser documentReferenceParser;

    DataModelMapper(Map<String, PoliticianDTO> politicianDTOS,
                    List<VoteDTO> voteDTOS,
                    List<PlenaryDTO> plenaryDTOS,
                    List<SummaryDTO> summaryDTOS) {
        requireNonNull(politicianDTOS);
        requireNonNull(voteDTOS);
        requireNonNull(plenaryDTOS);
        requireNonNull(summaryDTOS);
        this.plenaryDTOS = plenaryDTOS;
        this.voteDTOS = voteDTOS;
        this.voteCountFactory = new VoteCountFactory(politicianDTOS);
        this.documentReferenceParser = new DocumentReferenceParser(summaryDTOS);
    }

    public List<MotionGroup> buildAllMotionGroups() {
        return plenaryDTOS.stream()
                .sorted(PlenaryComparator.INSTANCE)
                .map(this::buildMotionGroups)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static MotionGroup mapMotionGroup(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO, List<Motion> motions) {
        return new MotionGroup(
                motionGroupDTO.id(),
                motionGroupDTO.title_nl(),
                motionGroupDTO.title_fr(),
                motions,
                plenaryDTO.date());
    }


    private List<MotionGroup> buildMotionGroups(PlenaryDTO plenaryDTO) {
        return plenaryDTO.motion_groups()
                .stream()
                .sorted(Comparator.comparing(MotionGroupDTO::plenary_agenda_item_number))
                .map(x -> this.mapMotionGroup(plenaryDTO, x))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<MotionGroup> mapMotionGroup(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        final var motions = mapMotions(plenaryDTO, motionGroupDTO);
        if (motions.isEmpty()) {
            logger.warn("No motions found for motion group {}, ignoring the motion group", motionGroupDTO.id());
            return Optional.empty();
        } else {
            return Optional.of(mapMotionGroup(plenaryDTO, motionGroupDTO, motions));
        }
    }

    private List<Motion> mapMotions(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        return motionGroupDTO.motions()
                .stream()
                .sorted(comparing(MotionDTO::sequence_number))
                .map(x -> buildMotion(x, plenaryDTO.id(), plenaryDTO.date()))
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<Motion> buildMotion(MotionDTO motionDTO, String plenaryId, String plenaryDate) {
        try {
            return buildMotionExpectingExceptions(motionDTO, plenaryId, plenaryDate);
        } catch (Throwable exception) {
            logger.error("Error occurred when building motion {}", motionDTO.id(), exception);
            return Optional.empty();
        }
    }

    private Optional<Motion> buildMotionExpectingExceptions(MotionDTO motionDTO, String plenaryId, String plenaryDate) {
        if (motionDTO.voting_id() == null) {
            logger.error("The motion {} from plenary {} has no votes assigned to it, so we are ignoring it", motionDTO.id(), plenaryId);
            return Optional.empty();
        }
        final Optional<VoteCount> voteCount = buildVoteCount(motionDTO);
        if (voteCount.isEmpty()) {
            logger.error("No VoteCount could be built for the motion {} from plenary {} so we are ignoring it.", motionDTO.id(), plenaryId);
            return Optional.empty();
        } else {
            return buildMotion(motionDTO, plenaryId, plenaryDate, voteCount.get());
        }
    }

    private Optional<Motion> buildMotion(MotionDTO motionDTO, String plenaryId, String plenaryDate, VoteCount voteCount) {
        final var builder = Motion.newBuilder().withDate(plenaryDate);
        builder.withVoteCount(voteCount);
        final var documentReference = documentReferenceParser.parseDocumentReference(motionDTO.documents_reference()).orElse(DocumentReference.NO_DOCUMENT);
        builder.withPlenaryId(plenaryId)
                .withMotionId(motionDTO.id())
                .withDocumentReference(motionDTO.documents_reference())
                .withNewDocumentReference(documentReference)
                //TODO there is no FR/NL discussion
                .withDescriptionFR(motionDTO.description())
                .withDescriptionNL(motionDTO.description())
                .withNumberInPlenary(motionDTO.sequence_number());

        fillTitle(motionDTO, builder);
        return Optional.of(builder.build());
    }

    private void fillTitle(MotionDTO motionDTO, Motion.Builder builder) {
        builder.withTitleFR(motionDTO.title_fr())
                .withTitleNL(motionDTO.title_nl());
    }

    private Optional<VoteCount> buildVoteCount(MotionDTO motionDTO) {
        final var motionVotes = getMotionVotes(motionDTO);
        return voteCountFactory.createVoteCount(motionVotes);
    }

    private List<VoteDTO> getMotionVotes(MotionDTO motionDTO) {
        return voteDTOS.stream().filter(v -> v.voting_id().equalsIgnoreCase(motionDTO.voting_id())).toList();
    }

}
