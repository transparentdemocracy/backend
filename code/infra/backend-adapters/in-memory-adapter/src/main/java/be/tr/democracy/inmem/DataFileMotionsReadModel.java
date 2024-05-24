package be.tr.democracy.inmem;

import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
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
    private final List<Motion> allMotionsReadModel;

    public DataFileMotionsReadModel(Supplier<List<PlenaryDTO>> plenariesSupplier,
                                    String votesFileName,
                                    String politiciansFileName) {
        this(createCachedSupplier(plenariesSupplier, votesFileName, politiciansFileName));
    }

    public DataFileMotionsReadModel(Supplier<List<Motion>> motions) {
        this.allMotionsReadModel = motions.get();
        logger.info("Motions read models loaded.");
    }

    public List<Motion> loadAll() {
        return allMotionsReadModel;
    }

    @Override
    public Page<Motion> find(String searchTerm, PageRequest pageRequest) {
        final var motions = findMotions(searchTerm);
        return Page.slicePageFromList(pageRequest, motions);
    }

    @Override
    public Optional<Motion> getMotion(String motionId) {
        return allMotionsReadModel.stream().filter(x -> x.motionId().equalsIgnoreCase(motionId)).findFirst();
    }

    private static LocalFileCache<Motion> createCachedSupplier(Supplier<List<PlenaryDTO>> plenariesSupplier, String votesFileName, String politiciansFileName) {
        final Supplier<List<Motion>> motionSupplier = () -> MotionsReadModelFactory.INSTANCE.create(plenariesSupplier, votesFileName, politiciansFileName);
        return new LocalFileCache<Motion>(motionSupplier, new File("target"), MOTION_CACHE_JSON, Motion.class);
    }

    private static Predicate<Motion> createFilter(String searchTerm) {
        return x -> containsSearchTerm(x.titleNL(), searchTerm) ||
                    containsSearchTerm(x.titleFR(), searchTerm) ||
                    containsSearchTerm(x.descriptionFR(), searchTerm) ||
                    containsSearchTerm(x.descriptionNL(), searchTerm);
    }

    private static boolean containsSearchTerm(String subject, String searchTerm) {
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private List<Motion> findMotions(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return allMotionsReadModel
                    .stream()
                    .filter(createFilter(searchTerm)).toList();
        } else
            return allMotionsReadModel;
    }


}
