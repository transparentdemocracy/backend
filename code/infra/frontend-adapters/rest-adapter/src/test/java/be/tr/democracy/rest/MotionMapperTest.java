package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.motion.VoteCount;
import org.junit.jupiter.api.Test;

import java.util.List;

import static be.tr.democracy.rest.MotionsMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MotionMapperTest {

    @Test
    void mapping() {
        final MotionViewDTO mappedDTO = MotionMapper.map(FIRST_MOTION);

        assertEquals(FIRST_MOTION.motionId(), mappedDTO.id());
        assertEquals(FIRST_MOTION.titleNL(), mappedDTO.titleNL());
        assertEquals(FIRST_MOTION.titleFR(), mappedDTO.titleFR());
        assertEquals(FIRST_MOTION.descriptionNL(), mappedDTO.descriptionNL());
        assertEquals(FIRST_MOTION.descriptionFR(), mappedDTO.descriptionFR());
        assertEquals(FIRST_MOTION.votingDate(), mappedDTO.votingDate());

        final VoteCount voteCount = FIRST_MOTION.voteCount();
        assertEquals(voteCount.yesVotes().nrOfVotes(), mappedDTO.yesVotes().nrOfVotes());
        assertEquals(voteCount.noVotes().nrOfVotes(), mappedDTO.noVotes().nrOfVotes());
        assertEquals(voteCount.abstention().nrOfVotes(), mappedDTO.absVotes().nrOfVotes());

        // 7 votes were casted in total, see below.

        // 1 abstention vote from the Cronos party:
        final var abstentionPartyVotes = mappedDTO.absVotes().partyVotes();

        final PartyVotesViewDTO guidoPartyAbstentionVotes = abstentionPartyVotes.stream().filter(
                pv -> pv.partyName().equals("GUIDO")).toList().getFirst();
        assertEquals(GUIDO_PARTY, guidoPartyAbstentionVotes.partyName());
        assertEquals(0, guidoPartyAbstentionVotes.numberOfVotes());
        assertEquals(0, guidoPartyAbstentionVotes.votePercentage());

        final PartyVotesViewDTO frankyPartyAbstentionVotes = abstentionPartyVotes.stream().filter(
                pv -> pv.partyName().equals("FRANKY")).toList().getFirst();
        assertEquals(FRANKY_PARTY, frankyPartyAbstentionVotes.partyName());
        assertEquals(0, frankyPartyAbstentionVotes.numberOfVotes());
        assertEquals(0, frankyPartyAbstentionVotes.votePercentage());

        final PartyVotesViewDTO cronosPartyAbstentionVotes = abstentionPartyVotes.stream().filter(
                pv -> pv.partyName().equals("CRONOS")).toList().getFirst();
        assertEquals(CRONOS_PARTY, cronosPartyAbstentionVotes.partyName());
        assertEquals(1, cronosPartyAbstentionVotes.numberOfVotes());
        assertEquals(14, cronosPartyAbstentionVotes.votePercentage());

        assertEquals(14, sumPercentagesAcrossPartiesWithinVoteType(abstentionPartyVotes));

        // 5 yes votes: 2 from the Guido party, 3 from the Franky party:
        final var yesPartyVotes = mappedDTO.yesVotes().partyVotes();

        final PartyVotesViewDTO guidoPartyYesVotes = yesPartyVotes.stream().filter(
                pv -> pv.partyName().equals("GUIDO")).toList().getFirst();
        assertEquals(GUIDO_PARTY, guidoPartyYesVotes.partyName());
        assertEquals(2, guidoPartyYesVotes.numberOfVotes());
        assertEquals(29, guidoPartyYesVotes.votePercentage());

        final PartyVotesViewDTO frankyPartyYesVotes = yesPartyVotes.stream().filter(
                pv -> pv.partyName().equals("FRANKY")).toList().getFirst();
        assertEquals(FRANKY_PARTY, frankyPartyYesVotes.partyName());
        assertEquals(3, frankyPartyYesVotes.numberOfVotes());
        assertEquals(43, frankyPartyYesVotes.votePercentage());

        final PartyVotesViewDTO cronosPartyYesVotes = yesPartyVotes.stream().filter(
                pv -> pv.partyName().equals("CRONOS")).toList().getFirst();
        assertEquals(CRONOS_PARTY, cronosPartyYesVotes.partyName());
        assertEquals(0, cronosPartyYesVotes.numberOfVotes());
        assertEquals(0, cronosPartyYesVotes.votePercentage());

        assertEquals(72, sumPercentagesAcrossPartiesWithinVoteType(yesPartyVotes));

        // 1 no vote, from the Guido party:
        final var noPartyVotes = mappedDTO.noVotes().partyVotes();

        final PartyVotesViewDTO guidoPartyNoVotes = noPartyVotes.stream().filter(
                pv -> pv.partyName().equals("GUIDO")).toList().getFirst();
        assertEquals(GUIDO_PARTY, guidoPartyNoVotes.partyName());
        assertEquals(1, guidoPartyNoVotes.numberOfVotes());
        assertEquals(14, guidoPartyNoVotes.votePercentage());

        final PartyVotesViewDTO frankyPartyNoVotes = noPartyVotes.stream().filter(
                pv -> pv.partyName().equals("FRANKY")).toList().getFirst();
        assertEquals(FRANKY_PARTY, frankyPartyNoVotes.partyName());
        assertEquals(0, frankyPartyNoVotes.numberOfVotes());
        assertEquals(0, frankyPartyNoVotes.votePercentage());

        final PartyVotesViewDTO cronosPartyNoVotes = noPartyVotes.stream().filter(
                pv -> pv.partyName().equals("CRONOS")).toList().getFirst();
        assertEquals(CRONOS_PARTY, cronosPartyNoVotes.partyName());
        assertEquals(0, cronosPartyNoVotes.numberOfVotes());
        assertEquals(0, cronosPartyNoVotes.votePercentage());

        assertEquals(14, sumPercentagesAcrossPartiesWithinVoteType(noPartyVotes));
    }

    @Test
    void percentagesAmountToHundred() {
        final MotionViewDTO mappedDTO = MotionMapper.map(THIRD_MOTION);

        final var yesPartyVotes = mappedDTO.yesVotes().partyVotes();
        final var noPartyVotes = mappedDTO.noVotes().partyVotes();
        final var absPartyVotes = mappedDTO.absVotes().partyVotes();

        assertEquals(49, sumPercentagesAcrossPartiesWithinVoteType(yesPartyVotes));
        assertEquals(0, sumPercentagesAcrossPartiesWithinVoteType(noPartyVotes));
        assertEquals(49, sumPercentagesAcrossPartiesWithinVoteType(absPartyVotes));
    }

    @Test
    void mappingWithNoVotes() {
        final MotionViewDTO mappedDTO = MotionMapper.map(SECOND_MOTION);

        assertEquals(SECOND_MOTION.titleNL(), mappedDTO.titleNL());
        assertEquals(SECOND_MOTION.titleFR(), mappedDTO.titleFR());
        assertEquals(SECOND_MOTION.descriptionNL(), mappedDTO.descriptionNL());
        assertEquals(SECOND_MOTION.descriptionFR(), mappedDTO.descriptionFR());
        assertEquals(SECOND_MOTION.votingDate(), mappedDTO.votingDate());

        final VoteCount voteCount = SECOND_MOTION.voteCount();
        assertEquals(voteCount.yesVotes().nrOfVotes(), mappedDTO.yesVotes().nrOfVotes());
        assertEquals(voteCount.noVotes().nrOfVotes(), mappedDTO.noVotes().nrOfVotes());
        assertEquals(voteCount.abstention().nrOfVotes(), mappedDTO.absVotes().nrOfVotes());

        final var absPartyVotes = mappedDTO.absVotes().partyVotes();
        final PartyVotesViewDTO abstentedFirstPV = absPartyVotes.getFirst();
        assertEquals(GUIDO_PARTY, abstentedFirstPV.partyName());
        assertEquals(0, abstentedFirstPV.numberOfVotes());
        assertEquals(0, abstentedFirstPV.votePercentage());

        final PartyVotesViewDTO abstentedLastPV = mappedDTO.absVotes().partyVotes().getLast();
        assertEquals(CRONOS_PARTY, abstentedLastPV.partyName());
        assertEquals(1, abstentedLastPV.numberOfVotes());
        assertEquals(17, abstentedLastPV.votePercentage());

        final var yesPartyVotes = mappedDTO.yesVotes().partyVotes();
        final PartyVotesViewDTO firstYesPartyVotes = yesPartyVotes.getFirst();
        assertEquals(GUIDO_PARTY, firstYesPartyVotes.partyName());
        assertEquals(2, firstYesPartyVotes.numberOfVotes());
        assertEquals(33, firstYesPartyVotes.votePercentage());

        final var noPartyVotes = mappedDTO.noVotes().partyVotes();
        final PartyVotesViewDTO firstNoPartyVotes = noPartyVotes.getFirst();
        assertEquals(GUIDO_PARTY, firstNoPartyVotes.partyName());
        assertEquals(0, firstNoPartyVotes.numberOfVotes());
        assertEquals(0, firstNoPartyVotes.votePercentage());

        assertEquals(83, sumPercentagesAcrossPartiesWithinVoteType(yesPartyVotes));
        assertEquals(0, sumPercentagesAcrossPartiesWithinVoteType(noPartyVotes));
        assertEquals(17, sumPercentagesAcrossPartiesWithinVoteType(absPartyVotes));
    }

    private static int sumPercentagesAcrossPartiesWithinVoteType(List<PartyVotesViewDTO> partyVotes) {
        return partyVotes.stream().mapToInt(PartyVotesViewDTO::votePercentage).sum();
    }
}