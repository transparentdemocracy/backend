package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.VoteCount;
import org.junit.jupiter.api.Test;

import static be.tr.democracy.rest.MotionsMother.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MotionMapperTest {

    @Test
    void mapping() {
        final MotionViewDTO mappedDTO = MotionMapper.map(FIRST_MOTION);

        assertEquals(FIRST_MOTION.titleNL(), mappedDTO.titleNL());
        assertEquals(FIRST_MOTION.titleFR(), mappedDTO.titleFR());
        assertEquals(FIRST_MOTION.descriptionNL(), mappedDTO.descriptionNL());
        assertEquals(FIRST_MOTION.descriptionFR(), mappedDTO.descriptionFR());
        assertEquals(FIRST_MOTION.date(), mappedDTO.votingDate());

        final VoteCount voteCount = FIRST_MOTION.voteCount();
        assertEquals(voteCount.yesVotes().nrOfVotes(), mappedDTO.yesVotes().nrOfVotes());
        assertEquals(voteCount.noVotes().nrOfVotes(), mappedDTO.noVotes().nrOfVotes());
        assertEquals(voteCount.abstention().nrOfVotes(), mappedDTO.absVotes().nrOfVotes());

        final PartyVotesViewDTO abstentedFirstPV = mappedDTO.absVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, abstentedFirstPV.partyName());
        assertEquals(0, abstentedFirstPV.numberOfVotes());
        assertEquals(0, abstentedFirstPV.votePercentage());

        final PartyVotesViewDTO abstentedLastPV = mappedDTO.absVotes().partyVotes().getLast();
        assertEquals(CRONOS_PARTY, abstentedLastPV.partyName());
        assertEquals(1, abstentedLastPV.numberOfVotes());
        assertEquals(100, abstentedLastPV.votePercentage());

        final PartyVotesViewDTO firstYesPartyVotes = mappedDTO.yesVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, firstYesPartyVotes.partyName());
        assertEquals(2, firstYesPartyVotes.numberOfVotes());
        assertEquals(40, firstYesPartyVotes.votePercentage());

        final PartyVotesViewDTO firstNoPartyVotes = mappedDTO.noVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, firstNoPartyVotes.partyName());
        assertEquals(1, firstNoPartyVotes.numberOfVotes());
        assertEquals(100, firstNoPartyVotes.votePercentage());


    }

    @Test
    void mappingWithNoVotes() {
        final MotionViewDTO mappedDTO = MotionMapper.map(SECOND_MOTION);

        assertEquals(SECOND_MOTION.titleNL(), mappedDTO.titleNL());
        assertEquals(SECOND_MOTION.titleFR(), mappedDTO.titleFR());
        assertEquals(SECOND_MOTION.descriptionNL(), mappedDTO.descriptionNL());
        assertEquals(SECOND_MOTION.descriptionFR(), mappedDTO.descriptionFR());
        assertEquals(SECOND_MOTION.date(), mappedDTO.votingDate());

        final VoteCount voteCount = SECOND_MOTION.voteCount();
        assertEquals(voteCount.yesVotes().nrOfVotes(), mappedDTO.yesVotes().nrOfVotes());
        assertEquals(voteCount.noVotes().nrOfVotes(), mappedDTO.noVotes().nrOfVotes());
        assertEquals(voteCount.abstention().nrOfVotes(), mappedDTO.absVotes().nrOfVotes());

        final PartyVotesViewDTO abstentedFirstPV = mappedDTO.absVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, abstentedFirstPV.partyName());
        assertEquals(0, abstentedFirstPV.numberOfVotes());
        assertEquals(0, abstentedFirstPV.votePercentage());

        final PartyVotesViewDTO abstentedLastPV = mappedDTO.absVotes().partyVotes().getLast();
        assertEquals(CRONOS_PARTY, abstentedLastPV.partyName());
        assertEquals(1, abstentedLastPV.numberOfVotes());
        assertEquals(100, abstentedLastPV.votePercentage());

        final PartyVotesViewDTO firstYesPartyVotes = mappedDTO.yesVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, firstYesPartyVotes.partyName());
        assertEquals(2, firstYesPartyVotes.numberOfVotes());
        assertEquals(40, firstYesPartyVotes.votePercentage());

        final PartyVotesViewDTO firstNoPartyVotes = mappedDTO.noVotes().partyVotes().getFirst();
        assertEquals(GUIDO_PARTY, firstNoPartyVotes.partyName());
        assertEquals(0, firstNoPartyVotes.numberOfVotes());
        assertEquals(0, firstNoPartyVotes.votePercentage());


    }
}