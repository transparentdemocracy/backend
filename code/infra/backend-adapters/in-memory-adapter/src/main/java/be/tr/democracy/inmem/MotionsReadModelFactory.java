package be.tr.democracy.inmem;

import be.tr.democracy.vocabulary.motion.MotionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

enum MotionsReadModelFactory {
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(MotionsReadModelFactory.class);

    public List<MotionGroup> create(Supplier<List<PlenaryDTO>> plenariesFileName, String votesFileName, String politiciansFileName, String summariesFileName) {
        logger.trace("Loading DataFileMotions from {}", plenariesFileName);
        final var dataFileLoader = new JSONDataFileLoader();
        final List<PlenaryDTO> plenaryDTOS = plenariesFileName.get();

        final Map<String, PoliticianDTO> politicianDTOS = dataFileLoader.loadPolitician(politiciansFileName);
        final List<VoteDTO> voteDTOS = dataFileLoader.loadVotes(votesFileName);
        final List<SummaryDTO> summaryDTOS = dataFileLoader.loadSummaries(summariesFileName);

        logger.trace("Data loaded in memory.");
        final DataModelMapper dataModelMapper = new DataModelMapper(politicianDTOS, voteDTOS, plenaryDTOS, summaryDTOS);
        return buildAllMotionsReadModel(dataModelMapper);
    }

    private List<MotionGroup> buildAllMotionsReadModel(DataModelMapper dataModelMapper) {
        try {
            return dataModelMapper.buildAllMotionGroups();
        } catch (Throwable e) {
            logger.error("UNABLE TO BUILD READ MODELS: ", e);
            return List.of();
        }
    }

}
