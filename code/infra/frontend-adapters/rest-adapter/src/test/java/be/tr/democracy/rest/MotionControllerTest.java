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
        final var motionController = new MotionController(DummyMotionsService.INSTANCE);

        final var pageViewDTOMono = motionController.getMotions("", 2, 2);

        StepVerifier.create(pageViewDTOMono)
                .expectNextMatches(this::validatePage)
                .expectComplete()
                .verify();
    }


    private static List<MotionViewDTO> mapToViewDTOs(List<Motion> result) {
        return result.stream().map(MotionMapper::map).toList();
    }


    private boolean validatePage(PageViewDTO<MotionViewDTO> x) {
        assertNotNull(x);
        assertEquals(1, x.pageSize());
        assertEquals(1, x.totalPages());
        assertEquals(1, x.pageNr());
        final var result = MotionsMother.DUMMY_MOTIONS;
        final var expectedResult = mapToViewDTOs(result);
        assertEquals(expectedResult, x.values());
        return expectedResult.equals(x.values());
    }
}