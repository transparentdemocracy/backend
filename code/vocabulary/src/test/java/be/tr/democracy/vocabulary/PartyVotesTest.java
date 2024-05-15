package be.tr.democracy.vocabulary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartyVotesTest {

    @Test
    void invalidPartyVote() {
        assertThrows(IllegalArgumentException.class, () -> new PartyVotes(null, 20));
        assertThrows(IllegalArgumentException.class, () -> new PartyVotes("", 20));
        assertThrows(IllegalArgumentException.class, () -> new PartyVotes("  ", 20));
        assertThrows(IllegalArgumentException.class, () -> new PartyVotes("CD&V", -1));
        assertThrows(IllegalArgumentException.class, () -> new PartyVotes("CD&V", -50));
        assertDoesNotThrow(() -> new PartyVotes("CD&V", 20));
    }
}