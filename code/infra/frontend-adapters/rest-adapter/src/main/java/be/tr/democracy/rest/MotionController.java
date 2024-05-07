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
    private static final int MOTION_PAGE_SIZE = 20;
    private final Logger logger = LoggerFactory.getLogger(MotionController.class);
    private final MotionsService motionsService;

    public MotionController(MotionsService motionsService) {
        requireNonNull(motionsService);
        this.motionsService = motionsService;
    }

    @GetMapping("/motions/{id}")
    public Mono<MotionViewDTO> getMotionById(@PathVariable int id) {
//        final var hardCoded = HardCodedMotions.createHardCodedMotion(id);
        return Mono.just(loadMotions().stream().filter(x -> x.title().contains(id + "")).toList().getFirst());
    }

    @GetMapping("/motions/")
    public Flux<MotionViewDTO> getMotions() {
//        return getHardCodedMotions();
        return getActualMotions();
    }

    private static MotionViewDTO map(Motion x) {
        return new MotionViewDTO("Motion " + x.numberInPlenary() + " from Plenary " + x.plenaryId(), x.date(),
                x.description(), true);
    }

    /**
     * Get the actual parsed data from the json files which are loaded at startup.
     */
    private Flux<MotionViewDTO> getActualMotions() {
        logger.info("***** Get parsed motions **");
        final List<MotionViewDTO> motionViewDTOS = loadMotions();
        logger.info("***** Loaded {} motions **", motionViewDTOS.size());
        return Flux.fromStream(motionViewDTOS.stream());
    }

    /**
     * Temporary hardcoded data which is only here for quick development purposes
     */
    private Flux<MotionViewDTO> getHardCodedMotions() {
        logger.info("***** Get hardcoded motions **");
        final List<MotionViewDTO> motionViewDTOS = HardCodedMotions.getMotionViewDTOS();
        return Flux.fromStream(motionViewDTOS.stream());
    }

    private List<MotionViewDTO> loadMotions() {
        return this.motionsService.getMotions(MOTION_PAGE_SIZE)
                .stream().map(MotionController::map)
                .toList();
    }

}
