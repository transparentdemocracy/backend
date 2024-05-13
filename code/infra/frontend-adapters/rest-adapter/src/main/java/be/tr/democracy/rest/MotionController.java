package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

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

    @GetMapping("/motions/{searchTerm}")
    public Flux<MotionViewDTO> searchMotions(@PathVariable String searchTerm) {
        return Flux.fromStream(findMotion(searchTerm));
    }

    @GetMapping("/motions/")
    public Flux<MotionViewDTO> getMotions() {
        logger.info("***** Get parsed motions **");
        final List<MotionViewDTO> motionViewDTOS = loadMotions();
        logger.info("***** Loaded {} motions **", motionViewDTOS.size());
        return Flux.fromStream(motionViewDTOS.stream());
    }

    //TODO move to query
    private Stream<MotionViewDTO> findMotion(String searchTerm) {
        return loadMotions().stream().filter(x -> x.titleNL().toLowerCase().contains(searchTerm.toLowerCase()));
    }

    private List<MotionViewDTO> loadMotions() {
        return this.motionsService.getMotions(MOTION_PAGE_SIZE)
                .stream()
                .map(MotionMapper::map)
                .toList();
    }

}
