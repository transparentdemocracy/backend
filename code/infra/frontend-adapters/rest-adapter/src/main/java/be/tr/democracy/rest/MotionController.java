package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.page.PageRequest;
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
    public Mono<PageViewDTO<MotionGroupViewDTO>> getMotions(
            @RequestParam(value = "search", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "1") int size) {
        logger.trace("Getting motions for search [{}] for page [{}] of size [{}]", searchTerm, page, size);
        final var motionPage = this.motionsService.findMotions(searchTerm, new PageRequest(page, size));
        return Mono.just(MotionMapper.mapViewPage(motionPage));
    }


    @GetMapping("/motions/{id}")
    public Mono<MotionGroupViewDTO> getMotion(@PathVariable String id) {
        logger.trace("Getting motion for id {}", id);
        final var motion = this.motionsService.getMotionGroup(id);
        logMotions(id, motion);
        return motion
                .map(MotionMapper::map)
                .map(Mono::just)
                .orElseGet(Mono::empty);
    }

    private void logMotions(String id, Optional<MotionGroup> motion) {
        motion.ifPresentOrElse(motionViewDTO -> logger.trace("Found motion in motiongroup {}", motionViewDTO),
                () -> logger.trace("No motion found for id {}", id)
        );
    }
}
