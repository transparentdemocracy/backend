package be.tr.democracy.rest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;

class MotionControllerTest {

    @Test
    void testFluxStream() {
        final var values = new ArrayList<Integer>();
        values.add(1);
        values.add(2);
        final var integerFlux = Flux.fromStream(values.stream());
        values.add(3);
        values.add(4);
        values.add(5);

        StepVerifier.create(integerFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .expectComplete()
                .verify();
    }
}