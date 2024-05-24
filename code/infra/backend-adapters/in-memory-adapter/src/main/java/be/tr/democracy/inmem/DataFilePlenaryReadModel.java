package be.tr.democracy.inmem;

import be.tr.democracy.query.PlenariesReadModel;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import be.tr.democracy.vocabulary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DataFilePlenaryReadModel implements PlenariesReadModel {
    private static final String PLENARY_CACHE_JSON = "plenaryCache.json";
    private final Logger logger = LoggerFactory.getLogger(DataFilePlenaryReadModel.class);
    private final List<Plenary> allPlenariesReadModel;

    public DataFilePlenaryReadModel(Supplier<List<PlenaryDTO>> supplier) {
        this.allPlenariesReadModel = createCachedSupplier(supplier).get();
        logger.info("Plenary read models loaded.");
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
        return allPlenariesReadModel;
    }

    private LocalFileCache<Plenary> createCachedSupplier(Supplier<List<PlenaryDTO>> plenariesSupplier) {
        final Supplier<List<Plenary>> motionSupplier = () -> {
            final var plenaryDTOS = plenariesSupplier.get();
            return PlenaryMapper.INSTANCE.mapThem(plenaryDTOS);
        };
        return new LocalFileCache<Plenary>(motionSupplier, new File("target"), PLENARY_CACHE_JSON, Plenary.class);
    }


}
