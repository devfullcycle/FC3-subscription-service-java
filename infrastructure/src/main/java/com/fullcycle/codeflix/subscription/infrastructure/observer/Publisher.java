package com.fullcycle.codeflix.subscription.infrastructure.observer;

import java.util.ArrayList;
import java.util.List;

public class Publisher<T> {

    private final List<Subscriber<T>> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void register(final Subscriber<T> sub) {
        if (sub != null) {
            this.subscribers.add(sub);
        }
    }

    public boolean publish(T ev) {
        boolean success = true;
        for (final var sub : subscribers) {
            try {
                if (sub.test(ev)) {
                    sub.onEvent(ev);
                }
            } catch (Throwable t) {
                success = false;
            }
        }
        return success;
    }
}
