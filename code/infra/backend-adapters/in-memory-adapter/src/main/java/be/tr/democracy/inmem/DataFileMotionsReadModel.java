package be.tr.democracy.inmem;

import be.tr.democracy.query.MotionsReadModel;
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
                                    String cacheTargetFolder) {
        this(createCachedSupplier(cacheTargetFolder, plenariesSupplier, votesFileName, politiciansFileName));
    }

    public DataFileMotionsReadModel(Supplier<List<MotionGroup>> motions) {
        this.allMotionsReadModel = motions.get();
        logger.info("Motions read models loaded.");
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
                                                                    String politiciansFileName) {
        final Supplier<List<MotionGroup>> motionSupplier = () -> MotionsReadModelFactory.INSTANCE.create(plenariesSupplier, votesFileName, politiciansFileName);
        return new LocalFileCache<MotionGroup>(motionSupplier, new File(cacheTargetFolder), MOTION_CACHE_JSON, MotionGroup.class);
    }

    private static Predicate<MotionGroup> createFilter(String searchTerm) {
        return motionGroup -> motionGroup.motions().stream().anyMatch(createMotionFilter(searchTerm));
    }

    private static Predicate<Motion> createMotionFilter(String searchTerm) {
        return motion -> containsSearchTerm(motion.titleNL(), searchTerm) ||
                         containsSearchTerm(motion.titleFR(), searchTerm) ||
                         containsSearchTerm(motion.descriptionFR(), searchTerm) ||
                         containsSearchTerm(motion.descriptionNL(), searchTerm);
    }

    private static boolean containsSearchTerm(String subject, String searchTerm) {
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private List<MotionGroup> findMotions(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return allMotionsReadModel
                    .stream()
                    .filter(createFilter(searchTerm)).toList();
        } else
            return allMotionsReadModel;
    }


}
