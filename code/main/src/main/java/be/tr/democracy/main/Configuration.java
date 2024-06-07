package be.tr.democracy.main;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.inmem.DataFileMotionsReadModel;
import be.tr.democracy.inmem.DataFilePlenaryReadModel;
import be.tr.democracy.inmem.PlenaryDTOFileLoader;
import be.tr.democracy.query.MotionsQuery;
import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.query.PlenariesQuery;
import be.tr.democracy.query.PlenariesReadModel;
import org.springframework.context.annotation.Bean;

import static be.tr.democracy.main.DataLocations.*;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    MotionsService motionsQuery(MotionsReadModel motionsReadModel) {
        return new MotionsQuery(motionsReadModel);
    }

    @Bean
    PlenaryService plenaryQuery(PlenariesReadModel plenariesReadModel) {
        return new PlenariesQuery(plenariesReadModel);
    }

    @Bean
    PlenaryDTOFileLoader plenaryFileLoader() {
        return new PlenaryDTOFileLoader(DATA_PLENARIES_JSON);
    }

    @Bean
    DataFileMotionsReadModel dataFileQuery(PlenaryDTOFileLoader p) {
        return new DataFileMotionsReadModel(p, DATA_VOTES_JSON, DATA_POLITICIANS_JSON, DATA_SUMMARIES_JSON, DOMAIN_MODEL_CACHE_FOLDER);
    }

    @Bean
    DataFilePlenaryReadModel plenaryDataFileQuery(PlenaryDTOFileLoader p) {
        return new DataFilePlenaryReadModel(p, DOMAIN_MODEL_CACHE_FOLDER);
    }

}
