package be.tr.democracy.inmem;

import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.VoteCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DataFileMotionsReadModel implements MotionsReadModel {
    private final Logger logger = LoggerFactory.getLogger(DataFileMotionsReadModel.class);
    private final List<PlenaryDTO> plenaryDTOS;
    private final List<PoliticianDTO> politicianDTOS;
    private final List<VoteDTO> voteDTOS;
    private final List<Motion> allMotionsReadmodel;

    public DataFileMotionsReadModel(String plenariesFileName, String votesFileName, String politiciansFileName) {
        logger.info("Loading DataFileMotions from {}", plenariesFileName);
        final var dataFileLoader = new JSONDataFileLoader();
        plenaryDTOS = dataFileLoader.loadPlenaries(plenariesFileName);
        politicianDTOS = dataFileLoader.loadPolitician(politiciansFileName);
        voteDTOS = dataFileLoader.loadVotes(votesFileName);
        logger.info("Data loaded in memory.");
        allMotionsReadmodel = buildAllMotionsReadmodel();
        logger.info("Read Models build.");
    }

    private static int countVotes(List<VoteDTO> motionVotes, String voteType) {
        final var count = motionVotes.stream().filter(x -> x.vote_type().equalsIgnoreCase(voteType)).count();
        return Long.valueOf(count).intValue();
    }

    @Override
    public List<Motion> loadAll() {
        return allMotionsReadmodel;
    }

    private void mapProposalFields(Motion.Builder builder, ProposalDiscussionDTO propForMotion) {
        if (propForMotion.proposals().isEmpty())
            logger.error("There are no Proposals for the motion!");
        if (propForMotion.proposals().size() > 1)
            logger.warn("There are multiple Proposals for the motion. Not yet mapped correctly.");

        final var proposalsDTO = propForMotion.proposals().getFirst();
        builder.withPlenaryId(propForMotion.plenary_id())
                .withTitleFR(proposalsDTO.title_fr())
                .withTitleNL(proposalsDTO.title_nl())
                .withDocumentReference(proposalsDTO.document_reference())
                .withDescriptionFR(propForMotion.description_fr())
                .withDescriptionNL(propForMotion.description_nl());
    }

    private List<Motion> buildAllMotionsReadmodel() {
        try {
            return plenaryDTOS.stream()
                    .map(this::buildMotions)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

        } catch (Throwable e) {
            logger.error("UNABLE TO BUILD READ MODELS: ", e);
            return List.of();
        }
    }

    private List<Motion> buildMotions(PlenaryDTO plenaryDTO) {
        final List<Motion> result = new ArrayList<>();
        for (MotionsDTO motionsDTO : plenaryDTO.motions()) {
            buildMotion(plenaryDTO, motionsDTO).ifPresent(result::add);
        }
        return Collections.unmodifiableList(result);
    }

    private Optional<Motion> buildMotion(PlenaryDTO plenaryDTO, MotionsDTO motionsDTO) {
        try {
            final var builder = Motion.newBuilder().withDate(plenaryDTO.date());
            final int index = motionsDTO.number() - 1;
            if (plenaryDTO.proposal_discussions().size() > index) {
                final ProposalDiscussionDTO proposalDiscussionDTO1 = plenaryDTO.proposal_discussions().get(index);
                mapProposalFields(builder, proposalDiscussionDTO1);
                mapMotionsField(motionsDTO, builder);
                return Optional.of(builder.build());
            } else {
                logger.error("There is no proposal discussion for motion number {} and motions ID {} for plenary {}", motionsDTO.number(), motionsDTO.id(), plenaryDTO.id());
                return Optional.empty();
            }

        } catch (Throwable exception) {
            logger.error("Error occured when building motion {}", motionsDTO.id(), exception);
            return Optional.empty();
        }
    }

    private void mapMotionsField(MotionsDTO motionsDTO, Motion.Builder builder) {
        builder.withVoteCount(buildVoteCount(motionsDTO));
        builder.withMotionId(motionsDTO.id())
                .withNumberInPlenary(motionsDTO.number());
    }

    private VoteCount buildVoteCount(MotionsDTO motionsDTO) {
        final var motionVotes = getMotionVotes(motionsDTO);
        final var yesVotes = countVotes(motionVotes, "YES");
        final var noVotes = countVotes(motionVotes, "NO");
        final var absentVotes = countVotes(motionVotes, "ABSTENTION");
        if ((yesVotes + noVotes + absentVotes) != motionVotes.size()) {
            logger.error("The votes do not match. Total nr of votes {} yes: {} no: {} absent: {}", motionVotes.size(), yesVotes, noVotes, absentVotes);
        }
        return new VoteCount(yesVotes, noVotes, absentVotes);
    }

    private List<VoteDTO> getMotionVotes(MotionsDTO motionsDTO) {
        return voteDTOS.stream().filter(v -> v.motion_id().equals(motionsDTO.id())).toList();
    }
}
