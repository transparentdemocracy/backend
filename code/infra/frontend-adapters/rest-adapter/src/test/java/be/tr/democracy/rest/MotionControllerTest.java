package be.tr.democracy.rest;

import be.tr.democracy.vocabulary.Motion;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MotionControllerTest {

    @Test
    void pageMotions() {
        final var motionController = new MotionController(maximum -> MotionsMother.DUMMY_MOTIONS);

        final var pageViewDTOMono = motionController.getMotions("",2, 2);

        StepVerifier.create(pageViewDTOMono)
                .expectNextMatches(this::validateSecondPage)
                .expectComplete()
                .verify();
    }

    @Test
    void tooLargePage() {
        final var motionController = new MotionController(maximum -> MotionsMother.DUMMY_MOTIONS);

        final var pageViewDTOMono = motionController.getMotions("",5, 2);

        StepVerifier.create(pageViewDTOMono)
                .expectNextMatches(this::validateSecondPage)
                .expectComplete()
                .verify();
    }

    @Test
    void unevenMotionsInPage() {
        final var motionController = new MotionController(maximum -> MotionsMother.THREE_MOTIONS);

        final var pageViewDTOMono = motionController.getMotions("",2, 2);

        StepVerifier.create(pageViewDTOMono)
                .expectNextMatches(this::validateUnevenSecondPage)
                .expectComplete()
                .verify();
    }


    private static List<MotionViewDTO> mapToViewDTOs(List<Motion> result) {
        return result.stream().map(MotionMapper::map).toList();
    }

    private boolean validateUnevenSecondPage(PageViewDTO<MotionViewDTO> x) {
        assertNotNull(x);
        assertEquals(2, x.pageSize());
        assertEquals(2, x.totalPages());
        assertEquals(2, x.pageNr());
        final var result = List.of(MotionsMother.THIRD_MOTION);
        final var expectedResult = mapToViewDTOs(result);
        assertEquals(expectedResult, x.values());
        return expectedResult.equals(x.values());
    }

    private boolean validateSecondPage(PageViewDTO<MotionViewDTO> x) {
        assertNotNull(x);
        assertEquals(2, x.pageSize());
        assertEquals(2, x.totalPages());
        assertEquals(2, x.pageNr());
        final var result = List.of(MotionsMother.THIRD_MOTION, MotionsMother.FOURTH);
        final var expectedResult = mapToViewDTOs(result);
        assertEquals(expectedResult, x.values());
        return expectedResult.equals(x.values());
    }
}