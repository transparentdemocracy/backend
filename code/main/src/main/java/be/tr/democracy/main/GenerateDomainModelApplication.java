package be.tr.democracy.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.context.annotation.Configuration
public class GenerateDomainModelApplication {


    private static final Logger logger = LoggerFactory.getLogger(GenerateDomainModelApplication.class);

    public static void main(String[] args) {
        logger.info("Generating domain model to files in folder" + DataLocations.DOMAIN_MODEL_CACHE_FOLDER);
        final var configuration = new Configuration();
        final var plenaryDTOFileLoader = configuration.plenaryFileLoader();
        final var motions = configuration.dataFileQuery(plenaryDTOFileLoader);
        logger.info("Generating domain models done");
        logger.info("Generated {} motions ", motions.loadAll().size());
    }


}
