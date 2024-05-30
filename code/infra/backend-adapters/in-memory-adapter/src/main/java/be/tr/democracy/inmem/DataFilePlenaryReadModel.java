package be.tr.democracy.inmem;

import be.tr.democracy.query.PlenariesReadModel;
import be.tr.democracy.vocabulary.page.Page;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DataFilePlenaryReadModel implements PlenariesReadModel {
    private static final String PLENARY_CACHE_JSON = "plenaryCache.json";
    private final Logger logger = LoggerFactory.getLogger(DataFilePlenaryReadModel.class);
    private final List<Plenary> allPlenariesReadModel;

    public DataFilePlenaryReadModel(Supplier<List<PlenaryDTO>> supplier) {
        this.allPlenariesReadModel = createCachedSupplier(supplier).get();

        // Pre-sort all plenaries on descending id (="{legislature}_{plenary date}_{plenary number}") now,
        // such that it does not need to sort on every incoming request:
        this.allPlenariesReadModel.sort(Comparator.comparing(Plenary::id).reversed());

        logger.trace("Plenary read models loaded.");
    }

    @Override
    public Page<Plenary> find(String searchTerm, PageRequest pageRequest) {
        final var plenaries = findPlenaries(searchTerm);
        return Page.slicePageFromList(pageRequest, plenaries);
    }

    @Override
    public Optional<Plenary> getPlenary(String plenaryId) {
        return allPlenariesReadModel.stream().filter(x -> x.id().equalsIgnoreCase(plenaryId)).findFirst();
    }


    private List<Plenary> findPlenaries(String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return allPlenariesReadModel
                    .stream()
                    .filter(createPlenaryFilter(searchTerm))
                    .toList();
        }
        return allPlenariesReadModel;
    }

    private LocalFileCache<Plenary> createCachedSupplier(Supplier<List<PlenaryDTO>> plenariesSupplier) {
        final Supplier<List<Plenary>> motionSupplier = () -> {
            final var plenaryDTOS = plenariesSupplier.get();
            return PlenaryMapper.INSTANCE.mapThem(plenaryDTOS);
        };
        return new LocalFileCache<Plenary>(motionSupplier, new File("target"), PLENARY_CACHE_JSON, Plenary.class);
    }

    private static Predicate<Plenary> createPlenaryFilter(String searchTerm) {
        return plenary -> containsSearchTerm(plenary.id(), searchTerm) ||
                          containsSearchTerm(plenary.plenaryDate(), searchTerm) ||
                          containsSearchTerm(plenary.legislature(), searchTerm) ||
                          plenary.motions().stream().anyMatch(createMotionLinkFilter(searchTerm));
    }

    private static boolean containsSearchTerm(String subject, String searchTerm) {
        return subject.toLowerCase().contains(searchTerm.toLowerCase());
    }

    private static Predicate<MotionLink> createMotionLinkFilter(String searchTerm) {
        return motionLink -> containsSearchTerm(motionLink.motionId(), searchTerm) ||
                             containsSearchTerm(motionLink.titleNL(), searchTerm) ||
                             containsSearchTerm(motionLink.titleFR(), searchTerm);
    }
}
