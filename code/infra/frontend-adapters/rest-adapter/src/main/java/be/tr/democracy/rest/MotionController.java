package be.tr.democracy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
public class MotionController {
    private final Logger logger = LoggerFactory.getLogger(MotionController.class);

    public MotionController() {
        logger.info("***** HERE WE ARE!! **");
    }

    @GetMapping("/motions/{id}")
    public Mono<MotionViewDTO> getMotionById(@PathVariable int id) {
        logger.info("***** Get motion by id **");
        final var hardCoded = new MotionViewDTO(new ProposalViewDTO(id, "toffe jongen met id " + id));
        return Mono.just(hardCoded);
    }

    @GetMapping("/motions/")
    public Flux<MotionViewDTO> getMotions() {
        logger.info("***** Get motions **");
        return Flux.just(
                new MotionViewDTO(new ProposalViewDTO(1, "toffe jongen")),
                new MotionViewDTO(new ProposalViewDTO(2, "jongen")),
                new MotionViewDTO(new ProposalViewDTO(666, "Franky")),
                new MotionViewDTO(new ProposalViewDTO(3, "peeut"))
        );
    }


}
