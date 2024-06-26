package be.tr.democracy.rest;

import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.vocabulary.page.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static java.util.Objects.requireNonNull;

@CrossOrigin
@RestController
public class PlenaryController {
    private final Logger logger = LoggerFactory.getLogger(PlenaryController.class);
    private final PlenaryService plenaryService;

    public PlenaryController(PlenaryService plenaryService) {
        requireNonNull(plenaryService);
        this.plenaryService = plenaryService;
    }

    @GetMapping("/plenaries/")
    public Mono<PageViewDTO<PlenaryViewDTO>> getPlenaries(
            @RequestParam(value = "search", defaultValue = "") String searchTerm,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "1") int size) {
        logger.trace("Getting plenaries for search [{}] for page [{}] of size [{}]", searchTerm, page, size);
        final var motionPage = this.plenaryService.findPlenaries(searchTerm, new PageRequest(page, size));
        final var data = PlenaryViewMapper.mapViewPage(motionPage);
        return Mono.just(data);
    }


    @GetMapping("/plenaries/{id}")
    public Mono<PlenaryViewDTO> getPlenary(@PathVariable String id) {
        logger.trace("Getting plenaries for id {}", id);
        final var motion = this.plenaryService.getPlenary(id);
        return motion
                .map(PlenaryViewMapper::map)
                .map(Mono::just)
                .orElseGet(Mono::empty);
    }
}
