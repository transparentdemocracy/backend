package be.tr.democracy.vocabulary;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VoteCountTest {

    @Test
    void checkForIllegalVoteTypes() {
        assertThrows(IllegalArgumentException.class, () -> new VoteCount(ObjectMother.yesVotes, ObjectMother.yesVotes, ObjectMother.absVotes));
        assertThrows(IllegalArgumentException.class, () -> new VoteCount(ObjectMother.noVotes, ObjectMother.noVotes, ObjectMother.absVotes));
        assertThrows(IllegalArgumentException.class, () -> new VoteCount(ObjectMother.yesVotes, ObjectMother.noVotes, ObjectMother.yesVotes));
        assertThrows(IllegalArgumentException.class, () -> new VoteCount(ObjectMother.absVotes, ObjectMother.absVotes, ObjectMother.absVotes));
    }

    @Test
    void votePassed() {
    }

    @Test
    void nrOfYesVotes() {
        ;
    }

    @Test
    void nrOfNoVotes() {
    }

    @Test
    void nrOfAbsentees() {
    }
}