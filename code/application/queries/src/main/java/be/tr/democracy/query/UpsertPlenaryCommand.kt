package be.tr.democracy.query;

import static java.util.function.Function.identity;

import be.tr.democracy.api.UpsertPlenary;
import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.motion.SubDocument;
import be.tr.democracy.vocabulary.motion.VoteCount;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import be.tr.democracy.vocabulary.plenary.Politician;
import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO make transactional
public class UpsertPlenaryCommand implements UpsertPlenary {

    private final PlenaryWriteModel plenaryWriteModel;
    private final SubDocumentReadModel subDocumentReadModel;
    private final MotionGroupWriteModel motionGroupWriteModel;
    private final VoteReadModel voteReadModel;
    private final PoliticianReadModel politicianReadModel;

    public UpsertPlenaryCommand(PlenaryWriteModel plenaryWriteModel, SubDocumentReadModel subDocumentReadModel, MotionGroupWriteModel motionGroupWriteModel,
        VoteReadModel voteReadModel, PoliticianReadModel politicianReadModel) {
        this.plenaryWriteModel = plenaryWriteModel;
        this.subDocumentReadModel = subDocumentReadModel;
        this.motionGroupWriteModel = motionGroupWriteModel;
        this.voteReadModel = voteReadModel;
        this.politicianReadModel = politicianReadModel;
    }

    @Override
    public void upsert(Plenary plenary) {
        plenaryWriteModel.upsert(plenary);

        List<MotionGroup> motionGroups = enrich(plenary);
        motionGroupWriteModel.deleteByPlenaryId(plenary.id());
        motionGroups.forEach(motionGroupWriteModel::upsert);
    }

    private List<MotionGroup> enrich(Plenary plenary) {
        List<Pair<Integer, Integer>> subDocumentIds = gatherSubDocumentIds(plenary);

        Map<Pair<Integer, Integer>, SubDocument> subDocumentsById = subDocumentReadModel.findSubDocuments(subDocumentIds).stream()
            .collect(Collectors.toMap(this::getId, identity()));

        List<String> votingIds = getVotingIds(plenary);

        Map<String, Politician> politiciansById = politicianReadModel.findAll().stream()
            .collect(Collectors.toMap(Politician::id, identity()));
        Map<String, VoteCount> countsByVotingId = voteReadModel.getVoteCountsByVotingIds(votingIds, politiciansById);

        return plenary.motionsGroups().stream()
            .map(it -> enrichMotionGroup(plenary, it, subDocumentsById, countsByVotingId))
            .toList();
    }

    private MotionGroup enrichMotionGroup(Plenary plenary, MotionGroupLink motionGroupLink, Map<Pair<Integer, Integer>, SubDocument> subDocumentsById,
        Map<String, VoteCount> countsByVotingId) {
        return new MotionGroup(
            motionGroupLink.id(),
            plenary.id(),
            motionGroupLink.plenaryAgendaItemNumber(),
            motionGroupLink.titleNL(),
            motionGroupLink.titleFR(),
            motionGroupLink.documentReference(),
            motionGroupLink.motions().stream()
                .map(it2 -> enrichMotion(plenary, it2, subDocumentsById, countsByVotingId))
                .toList(),
            plenary.plenaryDate()
        );
    }

    private Motion enrichMotion(Plenary plenary, MotionLink motionLink, Map<Pair<Integer, Integer>, SubDocument> subDocumentsById,
        Map<String, VoteCount> countsByVotingId) {
        return new Motion(
            motionLink.motionId(),
            motionLink.titleNL(),
            motionLink.titleFR(),
            createDocumentsReference(motionLink.documentsReference(), subDocumentsById),
            "TODO description", // TODO where does this come from?
            plenary.plenaryDate(),
            countsByVotingId.get(motionLink.votingId()),
            motionLink.votingId(),
            motionLink.cancelled(),
            plenary.id(),
            motionLink.voteSeqNr()
        );
    }

    private DocumentReference createDocumentsReference(String reference, Map<Pair<Integer, Integer>, SubDocument> subDocumentsById) {
        // TODO write document reference parsing & enriching logic
        return new DocumentReference(
            reference,
            null,
            List.of()
        );
    }

    private List<String> getVotingIds(Plenary plenary) {
        throw new NotImplementedError("TODO");
    }

    private Pair<Integer, Integer> getId(SubDocument subDocument) {
        return new Pair(subDocument.getDocumentNr(), subDocument.getDocumentSubNr());
    }

    private List<Pair<Integer, Integer>> gatherSubDocumentIds(Plenary plenary) {
        throw new NotImplementedError("TODO");
    }
}
