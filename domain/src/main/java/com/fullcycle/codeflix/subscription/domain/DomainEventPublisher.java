package com.fullcycle.codeflix.subscription.domain;

@FunctionalInterface
public interface DomainEventPublisher {
    void publishEvent(DomainEvent event);
}
