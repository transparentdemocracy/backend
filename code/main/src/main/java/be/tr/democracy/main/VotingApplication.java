package be.tr.democracy.main;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.inmem.DataFileMotionsReadModel;
import be.tr.democracy.query.MotionsQuery;
import be.tr.democracy.query.MotionsReadModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "be.tr.democracy")
public class VotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingApplication.class, args);
    }

    @Bean
    MotionsService motionsQuery(MotionsReadModel motionsReadModel) {
        return new MotionsQuery(motionsReadModel);
    }

    @Bean
    MotionsReadModel dataFileQuery() {
        return new DataFileMotionsReadModel("data/plenaries.json","data/votes.json","data/politicians.json");
    }

}
