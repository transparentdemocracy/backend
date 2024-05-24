package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Motion;
import be.tr.democracy.vocabulary.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@CrossOrigin
@RestController
public class MotionController {
    private final Logger logger = LoggerFactory.getLogger(MotionController.class);
    private final MotionsService motionsService;

    public MotionController(MotionsService motionsService) {
        requireNonNull(motionsService);
        this.motionsService = motionsService;
    }

    @GetMapping("/motions/")
    public Mono<PageViewDTO<MotionViewDTO>> getMotions(@RequestParam(value = "search", required = false) String searchTerm,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        logger.info("Getting motions for search {}", searchTerm);
        logger.info("Getting motions for page {}", page);
        logger.info("Getting motions for size {}", size);
        final var motionPage = this.motionsService.findMotions(searchTerm, new PageRequest(page, size));
        return Mono.just(MotionMapper.mapViewPage(motionPage));
    }


    @GetMapping("/motions/{id}")
    public Mono<MotionViewDTO> getMotion(@PathVariable String id) {
        logger.info("Getting motion for id {}", id);
        final var motion = this.motionsService.getMotion(id);
        logMotions(id, motion);
        return motion
                .map(MotionMapper::map)
                .map(Mono::just)
                .orElseGet(Mono::empty);
    }

    private void logMotions(String id, Optional<Motion> motion) {
        motion.ifPresentOrElse(motionViewDTO -> logger.info("Found motion {}", motionViewDTO),
                () -> logger.info("No motion found for id {}", id)
        );
    }
}
