package be.tr.democracy.rest;

import be.tr.democracy.api.MotionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    }

    @GetMapping("/motions/")
    public Mono<PageViewDTO<MotionViewDTO>> getMotions(@RequestParam(value = "search", required = false) String searchTerm,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size) {
        logger.info("Getting motions for search {}", searchTerm);
        logger.info("Getting motions for page {}", page);
        logger.info("Getting motions for size {}", size);
        //TODO move to query
        final var stream = this.motionsService.getMotions(-1)
                .stream()
                .map(MotionMapper::map);
        if (searchTerm != null && !searchTerm.isEmpty()) {
            final var viewDTOS = stream
                    .filter(x -> x.titleNL().toLowerCase().contains(searchTerm.toLowerCase())).toList();
            return Mono.just(sliceInPage(page, size, viewDTOS));
        } else {
            final var list = stream.toList();
            return Mono.just(sliceInPage(page, size, list));
        }
    }


    //TODO move to query
    private static PageViewDTO<MotionViewDTO> sliceInPage(int page, int size, List<MotionViewDTO> motionViewDTOS) {
        //TODO refactor this, move pagination to query
        int totalPages = (int) Math.ceil((double) motionViewDTOS.size() / size);
        int requestedPage = Math.max(1, Math.min(totalPages, page));
        int startIndex = (requestedPage - 1) * size;
        final var pageResult = motionViewDTOS.subList(startIndex, Math.min(startIndex + size, motionViewDTOS.size()));
        final var result = new PageViewDTO<MotionViewDTO>(requestedPage, size, totalPages, pageResult);
        return result;
    }


}
