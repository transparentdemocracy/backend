package be.tr.democracy.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "be.tr.democracy")
public class VotingApplication {


    public static void main(String[] args) {
        SpringApplication.run(VotingApplication.class, args);
    }


}
