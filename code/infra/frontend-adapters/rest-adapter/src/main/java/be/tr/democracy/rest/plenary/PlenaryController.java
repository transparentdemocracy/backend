package be.tr.democracy.rest.plenary;

import static java.util.Objects.requireNonNull;

import be.tr.democracy.api.PlenaryService;
import be.tr.democracy.api.UpsertPlenary;
import be.tr.democracy.rest.PageViewDTO;
import be.tr.democracy.rest.PlenaryViewDTO;
import be.tr.democracy.rest.PlenaryViewMapper;
import be.tr.democracy.vocabulary.page.PageRequest;
import be.tr.democracy.vocabulary.plenary.MotionGroupLink;
import be.tr.democracy.vocabulary.plenary.MotionLink;
import be.tr.democracy.vocabulary.plenary.Plenary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
public class PlenaryController {
    private final Logger logger = LoggerFactory.getLogger(PlenaryController.class);
    private final PlenaryService plenaryService;
    private final UpsertPlenary upsertPlenary;

    public PlenaryController(PlenaryService plenaryService, UpsertPlenary upsertPlenary) {
        this.upsertPlenary = requireNonNull(upsertPlenary);
        this.plenaryService = requireNonNull(plenaryService);
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

    // TODO add security
    @PostMapping(value = "/plenaries", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> upsertPlenary(@RequestBody UpsertPlenaryJsonRequest plenary) {
        upsertPlenary.upsert(toPlenary(plenary));
        return Mono.empty();
    }

    private Plenary toPlenary(UpsertPlenaryJsonRequest request) {
        String plenaryId = "%d_%d".formatted(request.getLegislature(), request.getNumber());
        return new Plenary(
            plenaryId,
            "%s (L%d)".formatted(request.getNumber(), request.getLegislature()),
            Integer.toString(request.getLegislature()),
            request.getDate().toString(),
            request.getPdf_report_url(),
            request.getHtml_report_url(),
            request.getMotion_groups().stream()
                .map(this::toMotionGroup)
                .toList()
        );
    }

    private MotionGroupLink toMotionGroup(UpsertPlenaryJsonRequest.MotionGroup request) {
        return new MotionGroupLink(
            request.getId(),
            request.getTitle_nl(),
            request.getTitle_fr(),
            request.getMotions().stream()
                .map(this::toMotion)
                .toList()
        );
    }

    private MotionLink toMotion(UpsertPlenaryJsonRequest.Motion request) {
        // TODO copied from PlenaryMapper. Feels pretty hacky, add required properties at the source instead of parsing the id?
        final var id = request.getId();
        try {
            final var split = id.split("_");
            final var last = split.length - 1;
            final var voteSeqNr = Integer.parseInt(split[last].substring(1)) + 1;
            final var agendaSeqNr = Integer.parseInt(split[last - 1]);
            return new MotionLink(id, agendaSeqNr, voteSeqNr, request.getTitle_nl(), request.getTitle_fr());
        } catch (Throwable e) {
            // TODO when does this happen? let it blow up here and fix!
            logger.error("Error parsing the motion link, extracting the agendaSeqNr and voteSegNr", e);
            return new MotionLink(id, 0, 0, request.getTitle_nl(), request.getTitle_fr());
        }
    }
}
