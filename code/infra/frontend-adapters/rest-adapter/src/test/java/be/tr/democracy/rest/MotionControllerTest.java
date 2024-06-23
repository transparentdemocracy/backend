package be.tr.democracy.rest;

import be.tr.democracy.rest.motion.MotionController;
import be.tr.democracy.vocabulary.motion.MotionGroup;
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

    @Test
    void singleMotion() {
        final var motionController = new MotionController(DummyMotionsService.INSTANCE);

        final var pageViewDTOMono = motionController.getMotion("third");

        StepVerifier.create(pageViewDTOMono)
                .expectNextMatches(this::validateThirdMotion)
                .expectComplete()
                .verify();
    }

    private static List<MotionGroupViewDTO> mapToViewDTOs(List<MotionGroup> result) {
        return result.stream().map(MotionMapper::map).toList();
    }

    private boolean validateThirdMotion(MotionGroupViewDTO o) {
        final var thirdMotion = MotionMapper.map(MotionsMother.THIRD_MOTION);
        return o.motions().contains(thirdMotion);
    }

    private boolean validatePage(PageViewDTO<MotionGroupViewDTO> x) {
        assertNotNull(x);
        assertEquals(1, x.pageSize());
        assertEquals(1, x.totalPages());
        assertEquals(1, x.pageNr());

        final var expectedResult = mapToViewDTOs(MotionsMother.DUMMY_MOTION_GROUPS);
        assertEquals(expectedResult, x.values());
        return expectedResult.equals(x.values());
    }
}