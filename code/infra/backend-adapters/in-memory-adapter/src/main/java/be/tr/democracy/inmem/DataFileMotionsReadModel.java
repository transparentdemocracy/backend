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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DataFileMotionsReadModel implements MotionsReadModel {
    private static final String MOTION_CACHE_JSON = "motionCache.json";
    private final Logger logger = LoggerFactory.getLogger(DataFileMotionsReadModel.class);
    private final List<MotionGroup> allMotionsReadModel;

    public DataFileMotionsReadModel(Supplier<List<PlenaryDTO>> plenariesSupplier,
                                    String votesFileName,
                                    String politiciansFileName,
                                    String summariesFileName,
                                    String cacheTargetFolder) {
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
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private List<MotionGroup> findMotions(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            final var motionGroupStream = allMotionsReadModel
                    .parallelStream();
            final List<String> allSearchTerms = Arrays.stream(searchTerm.split("\\s")).toList();
            final var result = filterSingleSearchTerm(motionGroupStream, allSearchTerms);
            return result.toList();
        } else
            return allMotionsReadModel;
    }

    private Stream<MotionGroup> filterSingleSearchTerm(Stream<MotionGroup> source, List<String> searchTerms) {
        if (searchTerms.isEmpty()) {
            return source;
        }
        final var newStream = source.filter(createSearchFilter(searchTerms.getFirst()));
        return filterSingleSearchTerm(newStream, searchTerms.subList(1, searchTerms.size()));
    }


}
