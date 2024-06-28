package be.tr.democracy.query;

import be.tr.democracy.vocabulary.motion.SubDocument;
import kotlin.Pair;

import java.util.List;

public interface SubDocumentReadModel {

    List<SubDocument> findSubDocuments(List<String> subDocumentIds);
}
