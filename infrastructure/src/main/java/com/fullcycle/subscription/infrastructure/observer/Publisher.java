package com.fullcycle.subscription.infrastructure.observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher<T> {

    private final List<Subscriber<T>> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void register(final Subscriber<T> subscriber) {
        this.subscribers.add(subscriber);
    }

    public boolean publish(final T event) {
        boolean success = true;
        for (Subscriber<T> sub : subscribers) {
            try {
                if (sub.test(event)) {
                    sub.onEvent(event);
                    return success;
                }
            } catch (Throwable t) {
                success = false;
            }
        }
        return success;
    }
}
