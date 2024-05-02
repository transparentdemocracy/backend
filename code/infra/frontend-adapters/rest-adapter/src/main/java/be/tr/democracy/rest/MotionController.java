package be.tr.democracy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MotionController {
    private final Logger logger = LoggerFactory.getLogger(MotionController.class);

    public MotionController() {
        logger.info("***** HERE WE ARE!! **");
    }

    @GetMapping("/motions/{id}")
    public Mono<MotionViewDTO> getEmployeeById(@PathVariable String id) {
        final var hardCoded = new MotionViewDTO(id, "toffe jongen");
        return Mono.just(hardCoded);
    }
}
