package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.VoteCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

class DataModelMapper {

    private final Logger logger = LoggerFactory.getLogger(DataModelMapper.class);
    private final VoteCountFactory voteCountFactory;
    private final List<VoteDTO> voteDTOS;
    private final List<PlenaryDTO> plenaryDTOS;

    DataModelMapper(Map<String, PoliticianDTO> politicianDTOS, List<VoteDTO> voteDTOS, List<PlenaryDTO> plenaryDTOS) {
        requireNonNull(politicianDTOS);
        requireNonNull(voteDTOS);
        requireNonNull(plenaryDTOS);
        this.plenaryDTOS = plenaryDTOS;
        this.voteDTOS = voteDTOS;
        this.voteCountFactory = new VoteCountFactory(politicianDTOS);
    }

    public List<Motion> buildAllMotions() {
        return plenaryDTOS.stream()
                .map(this::buildMotions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Motion> buildMotions(PlenaryDTO plenaryDTO) {
        return plenaryDTO.motion_groups().stream()
                .map(x -> this.mapMotionGroup(plenaryDTO, x))
                .flatMap(Collection::stream)
                .flatMap(Optional::stream)
                .toList();
    }

    private List<Optional<Motion>> mapMotionGroup(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        return motionGroupDTO.motions().stream().map(x -> buildMotion(x, plenaryDTO.id(), plenaryDTO.date())).toList();
    }

    private Optional<Motion> buildMotion(MotionDTO motionDTO, String plenaryId, String plenaryDate) {
        try {
            if (motionDTO.voting_id() == null) {
                logger.error("The motion {} from plenary {} has no votes assigned to it, so we are ignoring it", motionDTO.id(), plenaryId);
                return Optional.empty();
            }
            final Optional<VoteCount> voteCount = buildVoteCount(motionDTO);
            if (voteCount.isEmpty()) {
                logger.error("No VoteCount could be built for the motion {} from plenary {} so we are ignoring it.", motionDTO.id(), plenaryId);
                return Optional.empty();
            }

            final var builder = Motion.newBuilder().withDate(plenaryDate);
            voteCount.ifPresent(builder::withVoteCount);
            builder.withPlenaryId(plenaryId)
                    .withMotionId(motionDTO.id())
                    .withDocumentReference(motionDTO.documents_reference())
                    //TODO there is no FR/NL discussion
                    .withDescriptionFR(motionDTO.description())
                    .withDescriptionNL(motionDTO.description())
                    .withNumberInPlenary(motionDTO.sequence_number());

            fillTitle(motionDTO, builder);
            return Optional.of(builder.build());

        } catch (Throwable exception) {
            logger.error("Error occurred when building motion {}", motionDTO.id(), exception);
            return Optional.empty();
        }
    }

    private void fillTitle(MotionDTO motionDTO, Motion.Builder builder) {
        final Optional<ProposalDTO> proposalDTO = findProposal(motionDTO.proposal_id());
        proposalDTO.ifPresentOrElse(
                proposal -> {
                    final var titleFR = (proposal.title_fr().isBlank()) ? motionDTO.title_fr() : proposal.title_fr();
                    final var titleNL = (proposal.title_nl().isBlank()) ? motionDTO.title_nl() : proposal.title_nl();
                    builder
                            .withTitleFR(titleFR)
                            .withTitleNL(titleNL);

                }, () -> {
                    logger.warn("No proposal was found with proposal id {} from motion {}.", motionDTO.proposal_id(), motionDTO.id());
                    builder
                            .withTitleFR(motionDTO.title_fr())
                            .withTitleNL(motionDTO.title_nl());
                }
        );
    }

    private Optional<ProposalDTO> findProposal(String proposalId) {
        return this.plenaryDTOS.stream()
                .map(PlenaryDTO::proposal_discussions)
                .flatMap(Collection::stream)
                .map(ProposalDiscussionDTO::proposals)
                .flatMap(Collection::stream)
                .filter(x -> x.id().equals(proposalId))
                .findFirst();
    }

    private Optional<VoteCount> buildVoteCount(MotionDTO motionDTO) {
        final var motionVotes = getMotionVotes(motionDTO);
        return voteCountFactory.createVoteCount(motionVotes);
    }

    private List<VoteDTO> getMotionVotes(MotionDTO motionDTO) {
        return voteDTOS.stream().filter(v -> v.voting_id().equalsIgnoreCase(motionDTO.voting_id())).toList();
    }
}
