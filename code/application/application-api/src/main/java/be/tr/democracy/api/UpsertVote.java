package be.tr.democracy.api;

import be.tr.democracy.vocabulary.vote.Vote;

public interface UpsertVote {

    void upsert(Vote vote);
}
