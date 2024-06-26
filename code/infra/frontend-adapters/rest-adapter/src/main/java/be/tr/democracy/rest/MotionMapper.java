package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.motion.PartyVotes;
import be.tr.democracy.vocabulary.motion.Votes;
import be.tr.democracy.vocabulary.page.Page;

import java.util.List;

public class MotionMapper {

    private MotionMapper() {
    }

    public static MotionGroupViewDTO map(MotionGroup group) {
        final var motionViewDTOS = group.motions().stream().map(MotionMapper::map).toList();
        // TODO pass voting data
        return new MotionGroupViewDTO(group.id(), group.titleNL(), group.titleFR(), motionViewDTOS, null);
    }

    public static MotionViewDTO map(Motion motion) {
        final var voteCount = motion.getVoteCount();
        final var documentReference = DocumentReferenceMapper.INSTANCE.mapDocumentReference(motion.getNewDocumentReference());
        return new MotionViewDTO(
            motion.getMotionId(),
            motion.getTitleNL(),
            motion.getTitleFR(),
                mapVotes(voteCount.yesVotes(), voteCount.totalVotes()),
                mapVotes(voteCount.noVotes(), voteCount.totalVotes()),
                mapVotes(voteCount.abstention(), voteCount.totalVotes()),
                documentReference,
                motion.getVoteDate(),
                voteCount.votePassed());
    }

    public static PageViewDTO<MotionGroupViewDTO> mapViewPage(Page<MotionGroup> motionPage) {
        final var motionViewDTOS = motionPage.values().stream().map(MotionMapper::map).toList();
        return new PageViewDTO<MotionGroupViewDTO>(motionPage.pageNr(),
                motionPage.pageSize(),
                motionPage.totalPages(),
                motionViewDTOS);
    }

    static double limitToTwoDecimals(double percentage) {
        return Math.floor(percentage * 100) / 100;
    }

    private static MotionGroupViewDTO toMotionGroup(MotionViewDTO motionViewDTO) {
        return new MotionGroupViewDTO(motionViewDTO.id(), motionViewDTO.titleNL(), motionViewDTO.titleFR(), List.of(motionViewDTO), motionViewDTO.votingDate());
    }

    private static VotesViewDTO mapVotes(Votes votes, int total) {
        final var nrOfVotes = votes.nrOfVotes();
        final var percentage = calculatePercentage(total, nrOfVotes);
        final var partyVotes = mapVoteCount(votes, total);
        return new VotesViewDTO(nrOfVotes, percentage, partyVotes);
    }

    private static List<PartyVotesViewDTO> mapVoteCount(Votes votes, int total) {
        return votes.partyVotes()
                .stream()
                .map(x -> buildPartyVotes(x, total))
                .toList();
    }

    private static PartyVotesViewDTO buildPartyVotes(PartyVotes x, int total) {
        final int numberOfVotes = x.numberOfVotes();
        final double i = calculatePercentage(total, (double) numberOfVotes);
        return new PartyVotesViewDTO(x.partyName(), i, numberOfVotes);
    }

    private static double calculatePercentage(int total, double numberOfVotes) {
        if (total == 0) {
            return 0;
        } else {
            final double percentage = (numberOfVotes / total) * 100;
            return limitToTwoDecimals(percentage);
        }
    }
}
