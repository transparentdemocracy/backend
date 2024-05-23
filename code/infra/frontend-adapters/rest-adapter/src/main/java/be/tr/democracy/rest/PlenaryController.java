package be.tr.democracy.rest;

import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.vocabulary.PageRequest;
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
    public Mono<PageViewDTO<PlenaryViewDTO>> getPlenaries(@RequestParam(value = "search", required = false) String searchTerm,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        logger.info("Getting plenaries for search {}", searchTerm);
        logger.info("Getting plenaries for page {}", page);
        logger.info("Getting plenaries for size {}", size);
        final var motionPage = this.plenaryService.findPlenaries(searchTerm, new PageRequest(page, size));
        return Mono.just(PlenaryMapper.mapViewPage(motionPage));
    }


    @GetMapping("/plenaries/{id}")
    public Mono<PlenaryViewDTO> getPlenary(@PathVariable String id) {
        logger.info("Getting plenaries for id {}", id);
        final var motion = this.plenaryService.getPlenary(id);
        return motion
                .map(PlenaryMapper::map)
                .map(Mono::just)
                .orElseGet(Mono::empty);
    }
}
