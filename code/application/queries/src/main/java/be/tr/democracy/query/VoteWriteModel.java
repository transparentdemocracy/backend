package be.tr.democracy.query;

import be.tr.democracy.vocabulary.vote.Vote;

public interface VoteWriteModel {

    void upsert(Vote vote);

}
