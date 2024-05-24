package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.Motion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

enum MotionsReadModelFactory {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(MotionsReadModelFactory.class);

    public List<Motion> create(Supplier<List<PlenaryDTO>> plenariesFileName, String votesFileName, String politiciansFileName) {
        logger.info("Loading DataFileMotions from {}", plenariesFileName);
        final var dataFileLoader = new JSONDataFileLoader();
        final List<PlenaryDTO> plenaryDTOS = plenariesFileName.get();
        final Map<String, PoliticianDTO> politicianDTOS = dataFileLoader.loadPolitician(politiciansFileName);
        final List<VoteDTO> voteDTOS = dataFileLoader.loadVotes(votesFileName);

        logger.info("Data loaded in memory.");
        final DataModelMapper dataModelMapper = new DataModelMapper(politicianDTOS, voteDTOS, plenaryDTOS);
        return buildAllMotionsReadModel(dataModelMapper);
    }

    private List<Motion> buildAllMotionsReadModel(DataModelMapper dataModelMapper) {
        try {
            return dataModelMapper.buildAllMotions();
        } catch (Throwable e) {
            logger.error("UNABLE TO BUILD READ MODELS: ", e);
            return List.of();
        }
    }

}
