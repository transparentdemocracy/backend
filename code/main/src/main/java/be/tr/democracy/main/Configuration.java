package be.tr.democracy.main;

import static be.tr.democracy.main.DataLocations.DATA_PLENARIES_JSON;
import static be.tr.democracy.main.DataLocations.DATA_POLITICIANS_JSON;
import static be.tr.democracy.main.DataLocations.DATA_SUMMARIES_JSON;
import static be.tr.democracy.main.DataLocations.DATA_VOTES_JSON;
import static be.tr.democracy.main.DataLocations.DOMAIN_MODEL_CACHE_FOLDER;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.api.UpsertDocumentSummary;
import be.tr.democracy.inmem.DataFileMotionsReadModel;
import be.tr.democracy.inmem.PlenaryDTOFileLoader;
import be.tr.democracy.inmem.PlenaryRepository;
import be.tr.democracy.inmem.PoliticianRepository;
import be.tr.democracy.inmem.SubDocumentRepository;
import be.tr.democracy.inmem.VoteRepository;
import be.tr.democracy.query.MotionsQuery;
import be.tr.democracy.query.MotionsReadModel;
import be.tr.democracy.query.PlenariesQuery;
import be.tr.democracy.query.PlenariesReadModel;
import be.tr.democracy.query.PlenaryWriteModel;
import be.tr.democracy.query.PoliticianWriteModel;
import be.tr.democracy.query.SubDocumentWriteModel;
import be.tr.democracy.query.UpsertDocumentSummaryCommand;
import be.tr.democracy.query.UpsertPlenaryCommand;
import be.tr.democracy.query.UpsertPoliticianCommand;
import be.tr.democracy.query.UpsertVoteCommand;
import be.tr.democracy.query.VoteWriteModel;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {
    // Usecases
    @Bean
    UpsertPlenaryCommand upsertPlenaryCommand(PlenaryWriteModel plenaryWriteModel) {
        return new UpsertPlenaryCommand(plenaryWriteModel);
    }

    @Bean
    UpsertPoliticianCommand upsertPoliticianCommand(PoliticianWriteModel politicianWriteModel) {
        return new UpsertPoliticianCommand(politicianWriteModel);
    }

    @Bean
    UpsertVoteCommand upsertVoteCommand(VoteWriteModel voteWriteModel) {
        return new UpsertVoteCommand(voteWriteModel);
    }

    @Bean
    UpsertDocumentSummary upsertSubDocumentCommand(SubDocumentWriteModel subDocumentWriteModel) {
        return new UpsertDocumentSummaryCommand(subDocumentWriteModel);
    }

    @Bean
    MotionsService motionsQuery(MotionsReadModel motionsReadModel) {
        return new MotionsQuery(motionsReadModel);
    }

    @Bean
    PlenaryService plenaryQuery(PlenariesReadModel plenariesReadModel) {
        return new PlenariesQuery(plenariesReadModel);
    }

    // Data ports
    @Bean
    PlenaryDTOFileLoader plenaryFileLoader() {
        return new PlenaryDTOFileLoader(DATA_PLENARIES_JSON);
    }

    @Bean
    DataFileMotionsReadModel dataFileQuery(PlenaryDTOFileLoader p) {
        return new DataFileMotionsReadModel(p, DATA_VOTES_JSON, DATA_POLITICIANS_JSON, DATA_SUMMARIES_JSON, DOMAIN_MODEL_CACHE_FOLDER);
    }

    @Bean
    PlenaryRepository plenaryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PlenaryRepository(jdbcTemplate);
    }

    @Bean
    PoliticianRepository politicianRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new PoliticianRepository(jdbcTemplate);
    }

    @Bean
    VoteRepository voteRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new VoteRepository(jdbcTemplate);
    }

    @Bean
    SubDocumentRepository subDocumentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new SubDocumentRepository(jdbcTemplate);
    }
}
