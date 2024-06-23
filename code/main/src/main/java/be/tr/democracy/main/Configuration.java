package be.tr.democracy.main;

import static be.tr.democracy.main.DataLocations.DATA_PLENARIES_JSON;
import static be.tr.democracy.main.DataLocations.DATA_POLITICIANS_JSON;
import static be.tr.democracy.main.DataLocations.DATA_SUMMARIES_JSON;
import static be.tr.democracy.main.DataLocations.DATA_VOTES_JSON;
import static be.tr.democracy.main.DataLocations.DOMAIN_MODEL_CACHE_FOLDER;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.inmem.DataFileMotionsReadModel;
import be.tr.democracy.inmem.DataFilePlenaryReadModel;
import be.tr.democracy.inmem.PlenaryDTOFileLoader;
import be.tr.democracy.inmem.PlenaryRepository;
import be.tr.democracy.query.MotionsQuery;
import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.query.PlenariesQuery;
import be.tr.democracy.query.PlenariesReadModel;
import be.tr.democracy.query.PlenaryWriteModel;
import be.tr.democracy.query.UpsertPlenaryCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
    UpsertPlenaryCommand upsertPlenaryCommand(PlenaryWriteModel plenaryWriteModel) {
        return new UpsertPlenaryCommand(plenaryWriteModel);
    }

    @Bean
    PlenaryRepository plenaryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PlenaryRepository(jdbcTemplate);
    }
}
