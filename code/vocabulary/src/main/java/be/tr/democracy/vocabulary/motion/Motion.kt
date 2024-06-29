package be.tr.democracy.vocabulary.motion

data class Motion(
    val motionId: String,
    val titleNL: String,
    val titleFR: String,
    // TODO rename to just 'documentReference'
    val newDocumentReference: DocumentReference?,
    val description: String,
    val voteDate: String,
    val voteCount: VoteCount?, // nullable because votes are sent separately, and we might not have them
    val votingId: String,
    val voteCancelled: Boolean,
    val plenaryId: String,
    val sequenceNumberInPlenary: Int,
)
