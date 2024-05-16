package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.PartyVotes;
import be.tr.democracy.vocabulary.VoteCount;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoteCountFactoryTest {

    @Test
    void createVoteCount() {

        final var voteCountFactory = new VoteCountFactory(DataModelMother.politicianDTOMap());

        final var result = voteCountFactory.createVoteCount(DataModelMother.exampleVoteDTO());

        assertThat(result, notNullValue());
        assertTrue(result.isPresent());
        result.ifPresent(voteCount -> {

            assertThat(voteCount.totalVotes(), is(6));
            assertThat(voteCount.yesVotes().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 2),
                    new PartyVotes(DataModelMother.SOCRATES, 0),
                    new PartyVotes(DataModelMother.CRONOS, 0)
            ));
            assertThat(voteCount.noVotes().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 1),
                    new PartyVotes(DataModelMother.SOCRATES, 2),
                    new PartyVotes(DataModelMother.CRONOS, 0)
            ));
            assertThat(voteCount.abstention().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 0),
                    new PartyVotes(DataModelMother.SOCRATES, 0),
                    new PartyVotes(DataModelMother.CRONOS, 1)
            ));
        });

    }

    @Test
    void invalidVoteType() {
        final var voteCountFactory = new VoteCountFactory(DataModelMother.politicianDTOMap());
        final var result = voteCountFactory.createVoteCount(DataModelMother.exampleInvalidVoteDTO());

        assertThat(result, notNullValue());
        assertTrue(result.isPresent());
        result.ifPresent(voteCount -> {
            assertThat(voteCount.totalVotes(), is(4));
            assertThat(voteCount.yesVotes().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 2),
                    new PartyVotes(DataModelMother.SOCRATES, 0),
                    new PartyVotes(DataModelMother.CRONOS, 0)
            ));
            assertThat(voteCount.noVotes().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 0),
                    new PartyVotes(DataModelMother.SOCRATES, 1),
                    new PartyVotes(DataModelMother.CRONOS, 0)
            ));
            assertThat(voteCount.abstention().partyVotes(), containsInAnyOrder(
                    new PartyVotes(DataModelMother.TRIPLE_D, 1),
                    new PartyVotes(DataModelMother.SOCRATES, 0),
                    new PartyVotes(DataModelMother.CRONOS, 0)
            ));
        });
    }

    @Test
    void handlesEmptyVoteCount() {
        final var voteCountFactory = new VoteCountFactory(DataModelMother.politicianDTOMap());
        final Optional<VoteCount> result = voteCountFactory.createVoteCount(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void checksForUniqueMotionId() {
        final var voteCountFactory = new VoteCountFactory(DataModelMother.politicianDTOMap());
        final Optional<VoteCount> result = voteCountFactory.createVoteCount(DataModelMother.exampleWithDifferentMotionIDs());

        assertTrue(result.isEmpty());
    }
}