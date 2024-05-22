package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.VoteCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

class DataModelMapper {

    private final Logger logger = LoggerFactory.getLogger(DataModelMapper.class);
    private final VoteCountFactory voteCountFactory;
    private final List<VoteDTO> voteDTOS;

    DataModelMapper(Map<String, PoliticianDTO> politicianDTOS, List<VoteDTO> voteDTOS) {
        requireNonNull(politicianDTOS);
        requireNonNull(voteDTOS);
        this.voteDTOS = voteDTOS;
        this.voteCountFactory = new VoteCountFactory(politicianDTOS);
    }

    List<Motion> buildMotions(PlenaryDTO plenaryDTO) {
        return plenaryDTO.motion_groups().stream()
                .map(x -> this.mapMotionGroup(plenaryDTO, x))
                .flatMap(Collection::stream)
                .flatMap(Optional::stream)
                .toList();
    }

    private List<Optional<Motion>> mapMotionGroup(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        return motionGroupDTO.motions().stream().map(x -> buildMotion(plenaryDTO, x, plenaryDTO.id())).toList();
    }

    private Optional<Motion> buildMotion(PlenaryDTO plenaryDTO, MotionDTO motionDTO, String plenaryId) {
        try {
            if(motionDTO.voting_id() == null)
            {
                logger.error("The motion {} from plenary {} has no votes assigned to it, so we are ignoring it", motionDTO.id(), plenaryDTO.id());
                return Optional.empty();
            }
            final var builder = Motion.newBuilder().withDate(plenaryDTO.date());
            final Optional<VoteCount> voteCount = buildVoteCount(motionDTO);
            voteCount.ifPresent(builder::withVoteCount);
            if (voteCount.isEmpty()) {
                logger.error("No VoteCount could be built for the motion {} from plenary {} so we are ignoring it.", motionDTO.id(), plenaryDTO.id());
                return Optional.empty();
            }
            builder.withPlenaryId(plenaryId)
                    .withMotionId(motionDTO.id())
                    .withTitleFR(motionDTO.title_fr())
                    .withTitleNL(motionDTO.title_nl())
                    .withDocumentReference(motionDTO.documents_reference())
                    //TODO there is no FR/NL discussion
                    .withDescriptionFR(motionDTO.description())
                    .withDescriptionNL(motionDTO.description())
                    .withNumberInPlenary(motionDTO.sequence_number());
            return Optional.of(builder.build());

        } catch (Throwable exception) {
            logger.error("Error occurred when building motion {}", motionDTO.id(), exception);
            return Optional.empty();
        }
    }

    private Optional<VoteCount> buildVoteCount(MotionDTO motionDTO) {
        final var motionVotes = getMotionVotes(motionDTO);
        return voteCountFactory.createVoteCount(motionVotes);
    }

    private List<VoteDTO> getMotionVotes(MotionDTO motionDTO) {
        return voteDTOS.stream().filter(v -> v.voting_id().equalsIgnoreCase(motionDTO.voting_id())).toList();
    }
}
