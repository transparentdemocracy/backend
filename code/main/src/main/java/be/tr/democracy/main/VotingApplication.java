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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "be.tr.democracy")
public class VotingApplication {

    private static final String DOMAIN_MODEL_CACHE_FOLDER = "target";

    public static void main(String[] args) {
        SpringApplication.run(VotingApplication.class, args);
    }

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
        return new PlenaryDTOFileLoader("data/plenaries.json");
    }

    @Bean
    DataFileMotionsReadModel dataFileQuery(PlenaryDTOFileLoader p) {
        return new DataFileMotionsReadModel(p, "data/votes.json", "data/politicians.json", "data/summaries.json", DOMAIN_MODEL_CACHE_FOLDER);
    }

    @Bean
    DataFilePlenaryReadModel plenaryDataFileQuery(PlenaryDTOFileLoader p) {
        return new DataFilePlenaryReadModel(p, DOMAIN_MODEL_CACHE_FOLDER);
    }

}
