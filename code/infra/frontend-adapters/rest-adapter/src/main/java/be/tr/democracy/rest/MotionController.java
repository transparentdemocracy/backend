package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import be.tr.democracy.vocabulary.Page;
import be.tr.democracy.vocabulary.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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


    //TODO move to query
    private static PageViewDTO<MotionViewDTO> sliceInPage(Page<MotionViewDTO> motionViewDTOS) {
        return new PageViewDTO<MotionViewDTO>(motionViewDTOS.pageNr(), motionViewDTOS.pageSize(), motionViewDTOS.totalPages(), motionViewDTOS.values());
    }


}
