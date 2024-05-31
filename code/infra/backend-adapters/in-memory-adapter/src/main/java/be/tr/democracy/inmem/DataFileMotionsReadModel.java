package be.tr.democracy.inmem;

import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DataFileMotionsReadModel implements MotionsReadModel {
    private static final String MOTION_CACHE_JSON = "motionCache.json";
    private final Logger logger = LoggerFactory.getLogger(DataFileMotionsReadModel.class);
    private final List<MotionGroup> allMotionsReadModel;

    public DataFileMotionsReadModel(Supplier<List<PlenaryDTO>> plenariesSupplier,
                                    String votesFileName,
                                    String politiciansFileName,
                                    String summariesFileName, String cacheTargetFolder) {
        this(createCachedSupplier(cacheTargetFolder, plenariesSupplier, votesFileName, politiciansFileName, summariesFileName));
    }

    public DataFileMotionsReadModel(Supplier<List<PlenaryDTO>> plenariesSupplier,
                                    String votesFileName,
                                    String politiciansFileName,
                                    String summariesFileName) {
        this(createSupplier(plenariesSupplier, votesFileName, politiciansFileName, summariesFileName));
    }

    public DataFileMotionsReadModel(Supplier<List<MotionGroup>> motions) {
        this.allMotionsReadModel = motions.get();
        logger.trace("Motions read models loaded.");
    }

    public List<MotionGroup> loadAll() {
        return allMotionsReadModel;
    }

    @Override
    public Page<MotionGroup> find(String searchTerm, PageRequest pageRequest) {
        final var motions = findMotions(searchTerm);
        return Page.slicePageFromList(pageRequest, motions);
    }

    @Override
    public Optional<MotionGroup> getMotion(String motionId) {
        return allMotionsReadModel.stream().filter(x -> x.containsMotion(motionId)).findFirst();
    }

    private static LocalFileCache<MotionGroup> createCachedSupplier(String cacheTargetFolder,
                                                                    Supplier<List<PlenaryDTO>> plenariesSupplier,
                                                                    String votesFileName,
                                                                    String politiciansFileName, String summariesFileName) {
        final Supplier<List<MotionGroup>> motionSupplier = createSupplier(plenariesSupplier, votesFileName, politiciansFileName, summariesFileName);
        return new LocalFileCache<>(motionSupplier, new File(cacheTargetFolder), MOTION_CACHE_JSON, MotionGroup.class);
    }

    private static Supplier<List<MotionGroup>> createSupplier(Supplier<List<PlenaryDTO>> plenariesSupplier, String votesFileName, String politiciansFileName, String summariesFileName) {
        return () -> MotionsReadModelFactory.INSTANCE.create(plenariesSupplier, votesFileName, politiciansFileName, summariesFileName);
    }

    private static Predicate<MotionGroup> createSearchFilter(String searchTerm) {
        return motionGroup -> motionGroup.motions().parallelStream().anyMatch(createMotionFilter(searchTerm));
    }

    private static Predicate<Motion> createMotionFilter(String searchTerm) {
        return motion -> containsSearchTerm(motion.titleNL(), searchTerm) ||
                         containsSearchTerm(motion.titleFR(), searchTerm) ||
                         documentSummaryContains(motion.newDocumentReference(), searchTerm) ||
                         containsSearchTerm(motion.descriptionFR(), searchTerm) ||
                         containsSearchTerm(motion.descriptionNL(), searchTerm);
    }

    private static boolean documentSummaryContains(DocumentReference documentReference, String searchTerm) {
        return !documentReference.subDocuments().stream()
                .filter(x -> containsSearchTerm(x.summaryFR(), searchTerm) || containsSearchTerm(x.summaryNL(), searchTerm))
                .toList()
                .isEmpty();
    }

    private static boolean containsSearchTerm(String subject, String searchTerm) {
        System.out.println("[" +subject + "] contains " + searchTerm +" is " + subject.toLowerCase().contains(searchTerm.toLowerCase()));
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private List<MotionGroup> findMotions(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return allMotionsReadModel
                    .stream()
                    .filter(createSearchFilter(searchTerm)).toList();
        } else
            return allMotionsReadModel;
    }


}
