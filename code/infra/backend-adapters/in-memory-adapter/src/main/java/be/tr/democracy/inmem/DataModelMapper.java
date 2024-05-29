package be.tr.democracy.inmem;

import static java.util.Objects.requireNonNull;

import be.tr.democracy.vocabulary.motion.DocumentReference;
import be.tr.democracy.vocabulary.motion.Motion;
import be.tr.democracy.vocabulary.motion.MotionGroup;
import be.tr.democracy.vocabulary.motion.SubDocument;
import be.tr.democracy.vocabulary.motion.VoteCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DataModelMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataModelMapper.class);

    private static Pattern DOCUMENT_REFERENCE_PATTERN = Pattern.compile("^(\\d{4})/(.*)");
    private static Pattern NUMERIC = Pattern.compile("^(\\d+)");
    private static Pattern NUMERIC_RANGE = Pattern.compile("^(\\d+)-(\\d+)");

    private final Logger logger = LoggerFactory.getLogger(DataModelMapper.class);
    private final VoteCountFactory voteCountFactory;
    private final List<VoteDTO> voteDTOS;
    private final List<PlenaryDTO> plenaryDTOS;
    private final Map<String, String> summariesNL;

    DataModelMapper(Map<String, PoliticianDTO> politicianDTOS, List<VoteDTO> voteDTOS, List<PlenaryDTO> plenaryDTOS, SummariesDTO summariesDTO) {
        requireNonNull(politicianDTOS);
        requireNonNull(voteDTOS);
        requireNonNull(plenaryDTOS);
        requireNonNull(summariesDTO);
        this.plenaryDTOS = plenaryDTOS;
        this.voteDTOS = voteDTOS;
        this.voteCountFactory = new VoteCountFactory(politicianDTOS);
        this.summariesNL = summariesDTO.summaries().stream()
            .collect(Collectors.toMap(
                SummaryDTO::id,
                SummaryDTO::summary
            ));
    }

    public List<MotionGroup> buildAllMotionGroups() {
        return plenaryDTOS.stream()
            .map(this::buildMotions)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<MotionGroup> buildMotions(PlenaryDTO plenaryDTO) {
        return plenaryDTO.motion_groups().stream()
            .map(x -> this.mapMotionGroup(plenaryDTO, x))
            .flatMap(Optional::stream)
            .toList();
    }

    private Optional<MotionGroup> mapMotionGroup(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        final var motions = mapMotions(plenaryDTO, motionGroupDTO);
        if (motions.isEmpty()) {
            //            logger.warn("No motions found for motion group {}, ignoring the motion group", motionGroupDTO.id());
            return Optional.empty();
        }
        return Optional.of(new MotionGroup(motionGroupDTO.id(),
            motionGroupDTO.title_nl(),
            motionGroupDTO.title_fr(),
            motions,
            plenaryDTO.date()));
    }

    private List<Motion> mapMotions(PlenaryDTO plenaryDTO, MotionGroupDTO motionGroupDTO) {
        return motionGroupDTO.motions()
            .stream()
            .map(x -> buildMotion(x, plenaryDTO.id(), plenaryDTO.date()))
            .flatMap(Optional::stream)
            .toList();
    }

    private Optional<Motion> buildMotion(MotionDTO motionDTO, String plenaryId, String plenaryDate) {
        try {
            if (motionDTO.voting_id() == null) {
                //                logger.error("The motion {} from plenary {} has no votes assigned to it, so we are ignoring it", motionDTO.id(), plenaryId);
                return Optional.empty();
            }
            final Optional<VoteCount> voteCount = buildVoteCount(motionDTO);
            if (voteCount.isEmpty()) {
                logger.error("No VoteCount could be built for the motion {} from plenary {} so we are ignoring it.", motionDTO.id(), plenaryId);
                return Optional.empty();
            }

            final var builder = Motion.newBuilder().withDate(plenaryDate);
            voteCount.ifPresent(builder::withVoteCount);
            builder.withPlenaryId(plenaryId)
                .withMotionId(motionDTO.id())
                .withDocumentReference(motionDTO.documents_reference())
                .withNewDocumentReference(parseDocumentReference(motionDTO.documents_reference()).orElse(null))
                //TODO there is no FR/NL discussion
                .withDescriptionFR(motionDTO.description())
                .withDescriptionNL(motionDTO.description())
                .withNumberInPlenary(motionDTO.sequence_number());

            fillTitle(motionDTO, builder);
            return Optional.of(builder.build());
        } catch (Throwable exception) {
            logger.error("Error occurred when building motion {}", motionDTO.id(), exception);
            return Optional.empty();
        }
    }

    private void fillTitle(MotionDTO motionDTO, Motion.Builder builder) {
        final Optional<ProposalDTO> proposalDTO = findProposal(motionDTO.proposal_id());
        proposalDTO.ifPresentOrElse(
            proposal -> {
                final var titleFR = (proposal.title_fr().isBlank()) ? motionDTO.title_fr() : proposal.title_fr();
                final var titleNL = (proposal.title_nl().isBlank()) ? motionDTO.title_nl() : proposal.title_nl();
                builder
                    .withTitleFR(titleFR)
                    .withTitleNL(titleNL);
            }, () -> {
                //                    logger.warn("No proposal was found with proposal id {} from motion {}.", motionDTO.proposal_id(), motionDTO.id());
                builder
                    .withTitleFR(motionDTO.title_fr())
                    .withTitleNL(motionDTO.title_nl());
            }
        );
    }

    private Optional<ProposalDTO> findProposal(String proposalId) {
        return this.plenaryDTOS.stream()
            .map(PlenaryDTO::proposal_discussions)
            .flatMap(Collection::stream)
            .map(ProposalDiscussionDTO::proposals)
            .flatMap(Collection::stream)
            .filter(x -> x.id().equals(proposalId))
            .findFirst();
    }

    private Optional<VoteCount> buildVoteCount(MotionDTO motionDTO) {
        final var motionVotes = getMotionVotes(motionDTO);
        return voteCountFactory.createVoteCount(motionVotes);
    }

    private List<VoteDTO> getMotionVotes(MotionDTO motionDTO) {
        return voteDTOS.stream().filter(v -> v.voting_id().equalsIgnoreCase(motionDTO.voting_id())).toList();
    }

    private Optional<DocumentReference> parseDocumentReference(String spec) {
        if (spec == null || spec.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(parse(spec, summariesNL));
    }

    public static DocumentReference parse(String documentReference, Map<String, String> summariesNL) {
        Matcher matcher = DOCUMENT_REFERENCE_PATTERN.matcher(documentReference);
        if (!matcher.matches()) {
            Matcher numMatcher = NUMERIC.matcher(documentReference);
            if (numMatcher.matches()) {
                return new DocumentReference(documentReference, mainDocumentUrl(Integer.parseInt(numMatcher.group(0))), List.of());
            } else {
                return new DocumentReference(documentReference, null, List.of());
            }
        }
        Integer documentNr = Integer.parseInt(matcher.group(1), 10);
        String subDocumentNrSpec = matcher.group(2);

        List<Integer> subDocumentNrs = getSubDocumentNumbers(subDocumentNrSpec);
        return new DocumentReference(documentReference,
            mainDocumentUrl(documentNr),
            subDocumentNrs.stream().map(
                it -> new SubDocument(
                    documentNr,
                    it,
                    subDocumentUrl(documentNr, it),
                    summariesNL.getOrDefault("55/%04d/%03d".formatted(documentNr, it), null),
                    summariesNL.getOrDefault("55/%04d/%03d".formatted(documentNr, it), null)
                )
            ).toList());
    }

    private static String mainDocumentUrl(Integer documentNr) {
        return "https://www.dekamer.be/kvvcr/showpage.cfm?section=/flwb&language=nl&cfm=/site/wwwcfm/flwb/flwbn.cfm?lang=N&legislat=55&dossierID=%04d"
            .formatted(documentNr);
    }

    private static String subDocumentUrl(Integer documentNr, Integer subDocumentNr) {
        return "https://www.dekamer.be/FLWB/PDF/55/0297/55K%04d%03d.pdf".formatted(documentNr, subDocumentNr);
    }

    private static List<Integer> getSubDocumentNumbers(String subDocumentNrSpec) {
        Matcher subMatcher = NUMERIC.matcher(subDocumentNrSpec);
        if (subMatcher.matches()) {
            return List.of(Integer.parseInt(subMatcher.group(1), 10));
        } else {
            Matcher subRangeMatcher = NUMERIC_RANGE.matcher(subDocumentNrSpec);
            if (subRangeMatcher.matches()) {
                int rangeStart = Integer.parseInt(subRangeMatcher.group(1), 10);
                int rangeEnd = Integer.parseInt(subRangeMatcher.group(2), 10);
                if ((rangeEnd - rangeStart) > 100) {
                    LOGGER.warn(
                        "Refusing to create a subdocument range with more than 100 documents while parsing subdocument range %s".formatted(subDocumentNrSpec));
                    return List.of();
                }
                return IntStream.rangeClosed(rangeStart, rangeEnd).boxed().toList();
            }
        }
        return List.of();
    }
}
