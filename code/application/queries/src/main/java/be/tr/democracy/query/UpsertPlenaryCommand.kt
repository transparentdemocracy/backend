package be.tr.democracy.query

import be.tr.democracy.api.UpsertPlenary
import be.tr.democracy.vocabulary.motion.DocumentReference.parseDocumentReference
import be.tr.democracy.vocabulary.motion.Motion
import be.tr.democracy.vocabulary.motion.MotionGroup
import be.tr.democracy.vocabulary.motion.SubDocument
import be.tr.democracy.vocabulary.motion.VoteCount
import be.tr.democracy.vocabulary.plenary.MotionGroupLink
import be.tr.democracy.vocabulary.plenary.MotionLink
import be.tr.democracy.vocabulary.plenary.Plenary
import be.tr.democracy.vocabulary.plenary.Politician
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

// TODO make transactional
class UpsertPlenaryCommand(
    private val plenaryWriteModel: PlenaryWriteModel,
    private val subDocumentReadModel: SubDocumentReadModel,
    private val motionGroupWriteModel: MotionGroupWriteModel,
    private val voteReadModel: VoteReadModel,
    private val politicianReadModel: PoliticianReadModel,
) : UpsertPlenary {
    override fun upsert(plenary: Plenary) {
        plenaryWriteModel.upsert(plenary)

        val motionGroups = enrich(plenary)
        motionGroupWriteModel.deleteByPlenaryId(plenary.id)
        motionGroups.forEach(Consumer { motionGroup: MotionGroup? ->
            motionGroupWriteModel.upsert(
                motionGroup!!
            )
        })
    }

    private fun enrich(plenary: Plenary): List<MotionGroup> {
        val subDocumentIds = gatherSubDocumentIds(plenary)

        val subDocumentsById = subDocumentReadModel.findSubDocuments(subDocumentIds)
            .associateBy { it.documentNr to it.documentSubNr }

        val votingIds = getVotingIds(plenary)

        val politiciansById = politicianReadModel.findAll().stream()
            .collect(Collectors.toMap(Politician::id, Function.identity()))
        val countsByVotingId = voteReadModel.getVoteCountsByVotingIds(votingIds, politiciansById)

        return plenary.motionsGroups.stream()
            .map { it: MotionGroupLink -> enrichMotionGroup(plenary, it, subDocumentsById, countsByVotingId) }
            .toList()
    }

    private fun enrichMotionGroup(
        plenary: Plenary, motionGroupLink: MotionGroupLink, subDocumentsById: Map<Pair<Int, Int>, SubDocument>,
        countsByVotingId: Map<String, VoteCount>,
    ): MotionGroup {
        return MotionGroup(
            motionGroupLink.id,
            plenary.id,
            motionGroupLink.plenaryAgendaItemNumber,
            motionGroupLink.titleNL,
            motionGroupLink.titleFR,
            motionGroupLink.documentReference,
            motionGroupLink.motions.stream()
                .map { it2: MotionLink -> enrichMotion(plenary, it2, subDocumentsById, countsByVotingId) }
                .toList(),
            plenary.plenaryDate
        )
    }

    private fun enrichMotion(
        plenary: Plenary, motionLink: MotionLink, subDocumentsById: Map<Pair<Int, Int>, SubDocument>,
        countsByVotingId: Map<String, VoteCount>,
    ): Motion {
        return Motion(
            motionLink.motionId,
            motionLink.titleNL,
            motionLink.titleFR,
            parseDocumentReference(motionLink.documentsReference, subDocumentsById),
            "TODO description",  // TODO where does this come from?
            plenary.plenaryDate,
            countsByVotingId[motionLink.votingId],
            motionLink.votingId,
            motionLink.cancelled,
            plenary.id,
            motionLink.voteSeqNr
        )
    }

    private fun getVotingIds(plenary: Plenary): List<String> {
        return plenary.motionsGroups.flatMap {
            it.motions.map {
                it.votingId
            }
        }
    }

    private fun gatherSubDocumentIds(plenary: Plenary): List<String> {
        return plenary.motionsGroups.flatMap {
            it.motions.flatMap { parseDocumentReference(it.documentsReference)?.subDocuments ?: emptyList() }
        }.map { it.documentId }
    }
}
