package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.exceptions.InternalErrorException;
import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import com.fullcycle.subscription.infrastructure.observer.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EventMediator {

    private static final Logger log = LoggerFactory.getLogger(EventMediator.class);

    private final EventJdbcRepository eventRepository;
    private final Publisher<DomainEvent> domainEventPublisher;

    public EventMediator(final EventJdbcRepository eventRepository, final Publisher<DomainEvent> domainEventPublisher) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.domainEventPublisher = Objects.requireNonNull(domainEventPublisher);
    }

    public void mediate(final Long eventId) {
        this.eventRepository.eventOfIdAndUnprocessed(eventId).ifPresentOrElse(event -> {
            var result = domainEventPublisher.publish(event);
            if (!result) {
                throw InternalErrorException.with("Failed to process event %s".formatted(eventId));
            }
            this.eventRepository.markAsProcessed(eventId);
        }, () -> {
            log.warn("Event not found [eventId:{}]", eventId);
        });
    }
}
