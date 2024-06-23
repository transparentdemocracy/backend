package be.tr.democracy.vocabulary.vote

import be.tr.democracy.vocabulary.motion.VoteType

data class Vote(val votingId: String, val politicianId: String, val voteType: VoteType)