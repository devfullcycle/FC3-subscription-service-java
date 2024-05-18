package com.fullcycle.subscription.infrastructure.observer;

import com.fullcycle.subscription.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class PublisherTest extends UnitTest {

    @Test
    public void testPublisherWithGenericSubscriber() {
        record GenericSubscriber(Predicate<Integer> test, List<Integer> vals) implements Subscriber<Integer> {
            @Override
            public boolean test(Integer ev) {
                return test.test(ev);
            }

            @Override
            public void onEvent(Integer ev) {
                vals.add(ev);
            }
        }

        var even = new GenericSubscriber(i -> i % 2 == 0, new ArrayList<>());
        var odd = new GenericSubscriber(i -> i % 2 != 0, new ArrayList<>());

        final var publisher = new Publisher<Integer>();
        publisher.register(even);
        publisher.register(odd);

        publisher.publish(1);
        Assertions.assertEquals(1, odd.vals.size());
        Assertions.assertEquals(0, even.vals.size());

        publisher.publish(2);
        Assertions.assertEquals(1, odd.vals.size());
        Assertions.assertEquals(1, even.vals.size());
    }
}