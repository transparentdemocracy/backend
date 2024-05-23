package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.Motion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

enum MotionsReadModelFactory {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(MotionsReadModelFactory.class);

    public List<Motion> create(Supplier<List<PlenaryDTO>> plenariesSupplier, String votesFileName, String politiciansFileName) {
        logger.info("Loading DataFileMotions from {}", plenariesSupplier);
        final var dataFileLoader = new JSONDataFileLoader();
        final List<PlenaryDTO> plenaryDTOS = plenariesSupplier.get();
        final Map<String, PoliticianDTO> politicianDTOS = dataFileLoader.loadPolitician(politiciansFileName);
        final List<VoteDTO> voteDTOS = dataFileLoader.loadVotes(votesFileName);

        logger.info("Data loaded in memory.");
        final DataModelMapper dataModelMapper = new DataModelMapper(politicianDTOS, voteDTOS);
        return buildAllMotionsReadModel(dataModelMapper, plenaryDTOS);
    }

    private List<Motion> buildAllMotionsReadModel(DataModelMapper dataModelMapper, List<PlenaryDTO> plenaryDTOS) {
        try {
            return plenaryDTOS.stream()
                    .map(dataModelMapper::buildMotions)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

        } catch (Throwable e) {
            logger.error("UNABLE TO BUILD READ MODELS: ", e);
            return List.of();
        }
    }

}
