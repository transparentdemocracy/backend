package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.motion.PartyVotes;
import be.tr.democracy.vocabulary.motion.Votes;

import java.util.List;

public class MotionMapper {

    private MotionMapper() {
    }

    public static MotionGroupViewDTO map(Motion motion) {
        final var voteCount = motion.voteCount();
        final var motionViewDTO = new MotionViewDTO(
                motion.motionId(),
                motion.titleNL(),
                motion.titleFR(),
                mapVotes(voteCount.yesVotes()),
                mapVotes(voteCount.noVotes()),
                mapVotes(voteCount.abstention()),
                motion.votingDate(),
                motion.descriptionNL(),
                motion.descriptionFR(),
                voteCount.votePassed());
        return toMotionGroup( motionViewDTO);
    }

    private static MotionGroupViewDTO toMotionGroup( MotionViewDTO motionViewDTO) {
        return new MotionGroupViewDTO(motionViewDTO.id(), motionViewDTO.titleNL(), motionViewDTO.titleFR(), List.of(motionViewDTO), motionViewDTO.votingDate());
    }

    public static PageViewDTO<MotionGroupViewDTO> mapViewPage(Page<Motion> motionPage) {
        final var motionViewDTOS = motionPage.values().stream().map(MotionMapper::map).toList();
        return new PageViewDTO<MotionGroupViewDTO>(motionPage.pageNr(),
                motionPage.pageSize(),
                motionPage.totalPages(),
                motionViewDTOS);
    }

    private static VotesViewDTO mapVotes(Votes yesVotes) {
        return new VotesViewDTO(yesVotes.nrOfVotes(), mapVoteCount(yesVotes));
    }

    private static List<PartyVotesViewDTO> mapVoteCount(Votes votes) {
        final int total = votes.nrOfVotes();
        return votes.partyVotes()
                .stream()
                .map(x -> buildPartyVotes(x, total))
                .toList();
    }

    private static PartyVotesViewDTO buildPartyVotes(PartyVotes x, int total) {
        final int numberOfVotes = x.numberOfVotes();
        final int i = calculatePercentage(total, (double) numberOfVotes);
        return new PartyVotesViewDTO(x.partyName(), i, numberOfVotes);
    }

    private static int calculatePercentage(int total, double numberOfVotes) {
        if (total == 0) return 0;
        else {
            final double percentage = (numberOfVotes / total) * 100;

            final var round = Math.round(percentage);
            return Long.valueOf(round).intValue();
        }
    }


}
