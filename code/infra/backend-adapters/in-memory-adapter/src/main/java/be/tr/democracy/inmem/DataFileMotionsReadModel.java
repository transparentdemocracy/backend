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

    public DataFileMotionsReadModel(String plenariesFileName, String votesFileName, String politiciansFileName) {
        logger.info("Loading DataFileMotions from {}", plenariesFileName);
        final var dataFileLoader = new JSONDataFileLoader();
        plenaryDTOS = dataFileLoader.loadPlenaries(plenariesFileName);
        politicianDTOS = dataFileLoader.loadPolitician(politiciansFileName);
        voteDTOS = dataFileLoader.loadVotes(votesFileName);
        logger.info("Data loaded in memory.");

    }

    @Override
    public List<Motion> loadAll() {
        return plenaryDTOS.stream()
                .map(this::buildMotions)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static int countVotes(List<VoteDTO> motionVotes, String voteType) {
        final var count = motionVotes.stream().filter(x -> x.vote_type().equalsIgnoreCase(voteType)).count();
        return Long.valueOf(count).intValue();
    }

    private List<Motion> buildMotions(PlenaryDTO plenaryDTO) {
        final List<Motion> result = new ArrayList<>();
        for (MotionsDTO motionsDTO : plenaryDTO.motions()) {
            buildMotion(plenaryDTO, motionsDTO).ifPresent(result::add);
        }
        return Collections.unmodifiableList(result);
    }

    private Optional<Motion> buildMotion(PlenaryDTO plenaryDTO, MotionsDTO motionsDTO) {
        final var proposalId = motionsDTO.proposal_id();
        final var allProposals = plenaryDTO.proposals().stream().filter(proposalsDTO -> proposalsDTO.id().equals(proposalId)).toList();
        if (allProposals.isEmpty()) {
            logger.error("No matching proposal was found for proposalId {} for the motion {}", proposalId, motionsDTO.id());
        } else if (allProposals.size() > 1) {
            logger.error("More than one proposal was found for proposalId {} for the motion {}", proposalId, motionsDTO.id());
        } else {
            final var propForMotion = allProposals.getFirst();
            return Optional.of(buildMotion(motionsDTO, propForMotion));
        }
        return Optional.empty();
    }

    private Motion buildMotion(MotionsDTO motionsDTO, ProposalsDTO propForMotion) {
        final var voteCount = buildVoteCount(motionsDTO);
        return new Motion(motionsDTO.id(), propForMotion.plenary_id(), motionsDTO.number(), propForMotion.description(), voteCount);
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
