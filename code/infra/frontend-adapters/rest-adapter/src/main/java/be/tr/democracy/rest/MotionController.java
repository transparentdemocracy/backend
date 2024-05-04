package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Objects.requireNonNull;

@CrossOrigin
@RestController
public class MotionController {
    private final Logger logger = LoggerFactory.getLogger(MotionController.class);
    private final MotionsService motionsService;

    public MotionController(MotionsService motionsService) {
        requireNonNull(motionsService);
        this.motionsService = motionsService;

        logger.info("Motions controller has {} motions", motionsService.getMotions().size());
    }

    @GetMapping("/motions/{id}")
    public Mono<MotionViewDTO> getMotionById(@PathVariable int id) {
        final var hardCoded = HardCodedMotions.createHardCodedMotion(id);
        return Mono.just(hardCoded);
    }

    @GetMapping("/motions/")
    public Flux<List<MotionViewDTO>> getMotions() {
        return getHardCodedMotions();
        //return getActualMotions();
    }

    private static MotionViewDTO map(Motion x) {
        return new MotionViewDTO("Motion " + x.numberInPlenary() + " from Plenary " + x.plenaryId(), "01/01/1830",
                "This is an example description of a motion", true);
    }

    /**
     * Get the actual parsed data from the json files which are loaded at startup.
     */
    private Flux<List<MotionViewDTO>> getActualMotions() {
        logger.info("***** Get parsed motions **");
        final List<MotionViewDTO> motionViewDTOS = loadMotions();
        return Flux.just(motionViewDTOS);
    }

    /**
     * Temporary hardcoded data which is only here for quick development purposes
     */
    private Flux<List<MotionViewDTO>> getHardCodedMotions() {
        logger.info("***** Get hardcoded motions **");
        final List<MotionViewDTO> motionViewDTOS = HardCodedMotions.getMotionViewDTOS();
        return Flux.just(motionViewDTOS);
    }

    private List<MotionViewDTO> loadMotions() {
        return this.motionsService.getMotions()
                .stream().map(MotionController::map)
                .toList();
    }

}
