package com.fullcycle.subscription.infrastructure.mediator;

import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EventMediator {

    private static final Logger log = LoggerFactory.getLogger(EventMediator.class);

    private final EventJdbcRepository eventRepository;

    public EventMediator(final EventJdbcRepository eventRepository) {
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    public void mediate(final Long eventId) {
        this.eventRepository.eventOfId(eventId).ifPresentOrElse(event -> {
            // TODO: process
            this.eventRepository.markAsProcessed(eventId);
        }, () -> {
            log.warn("Event not found [eventId:{}]", eventId);
        });
    }
}
