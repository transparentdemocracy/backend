package be.tr.democracy.main;

import be.tr.democracy.api.FindMotions;
import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.api.UpsertDocumentSummary;
import be.tr.democracy.inmem.MotionGroupRepository;
import be.tr.democracy.inmem.PlenaryRepository;
import be.tr.democracy.inmem.PoliticianRepository;
import be.tr.democracy.inmem.SubDocumentRepository;
import be.tr.democracy.inmem.VoteRepository;
import be.tr.democracy.query.FindMotionsQuery;
import be.tr.democracy.query.MotionGroupReadModel;
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
    FindMotions motionsQuery(MotionGroupReadModel motionGroupReadModel) {
        return new FindMotionsQuery(motionGroupReadModel);
    }

    @Bean
    PlenaryService plenaryQuery(PlenariesReadModel plenariesReadModel) {
        return new PlenariesQuery(plenariesReadModel);
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

    @Bean
    MotionGroupRepository motionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new MotionGroupRepository(jdbcTemplate);
    }
}
