package com.fullcycle.codeflix.subscription.infrastructure.observer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PublisherTest {

    @Test
    public void testPublisherWithGenericSubscriber() {
        final var odds = new AtomicInteger(0);
        final var evens = new AtomicInteger(0);

        final var target = new Publisher<Integer>();
        target.register(new GenericSubscriber<>(i -> i % 2 != 0, i -> odds.incrementAndGet()));
        target.register(new GenericSubscriber<>(i -> i % 2 == 0, i -> evens.incrementAndGet()));

        target.publish(2);
        Assertions.assertEquals(1, evens.get());
        Assertions.assertEquals(0, odds.get());

        target.publish(3);
        Assertions.assertEquals(1, evens.get());
        Assertions.assertEquals(1, odds.get());

        target.publish(4);
        Assertions.assertEquals(2, evens.get());
        Assertions.assertEquals(1, odds.get());

    }

    record GenericSubscriber<T>(Predicate<T> predicate, Consumer<T> consumer) implements Subscriber<T> {

        @Override
        public boolean test(T ev) {
            return this.predicate.test(ev);
        }

        @Override
        public void onEvent(T ev) {
            this.consumer.accept(ev);
        }
    }
}
