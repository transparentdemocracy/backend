package be.tr.democracy.vocabulary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VotesTest {

    @Test
    void validVotes() {
        final var votes = new Votes(VoteType.YES, 666, ObjectMother.GUIDO_PARTY);

        final var nice = Votes.yesVotes(666, ObjectMother.GUIDO_PARTY);

        assertEquals(votes, nice);
    }

    @Test
    void checkForInvalidVotes() {
        assertThrows(IllegalArgumentException.class, () -> new Votes(VoteType.YES, -20, ObjectMother.GUIDO_PARTY));
        assertThrows(IllegalArgumentException.class, () -> new Votes(VoteType.YES, 50, ObjectMother.GUIDO_PARTY));
        assertThrows(IllegalArgumentException.class, () -> new Votes(VoteType.YES, 50, ObjectMother.FRANKY_PARTY));
        assertDoesNotThrow(() -> new Votes(VoteType.YES, 100, ObjectMother.FRANKY_PARTY));
    }
}