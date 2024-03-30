package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.json.Json;
import com.fullcycle.codeflix.subscription.infrastructure.database.DatabaseClient;
import com.fullcycle.codeflix.subscription.infrastructure.database.RowMapping;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class EventRepository {

    private static final RowMapping<Event> eventRowMapping = eventRowMapping();

    private final DatabaseClient database;

    public EventRepository(final DatabaseClient database) {
        this.database = Objects.requireNonNull(database);
    }

    public Optional<DomainEvent> eventOfId(final String eventId) {
        final var sql = "SELECT event_id, processed, aggregate_id, aggregate_type, event_type, event_date, event_data FROM events WHERE id = :id";
        final var rows = this.database.query(sql, Map.of("id", eventId), eventRowMapping);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDomainEvent(rows.getFirst()));
    }

    public List<DomainEvent> allEventsOfAggregate(final String aggregateType, final String aggregateId) {
        final var sql = """
                SELECT event_id, processed, aggregate_id, aggregate_type, event_type, event_date, event_data
                FROM events
                WHERE aggregate_type = :aggregateType AND aggregate_id = :aggregateId
                """;

        final var params = Map.<String, Object>of(
                "aggregateType", aggregateType,
                "aggregateId", aggregateId
        );

        return this.database.query(sql, params, eventRowMapping).stream()
                .map(EventRepository::toDomainEvent)
                .toList();
    }

    public void saveAll(final Collection<DomainEvent> events) {
        for (final var ev : events) {
            this.insertEvent(Event.newEvent(ev.aggregateId(), ev.aggregateType(), ev.getClass().getCanonicalName(), Json.writeValueAsBytes(ev)));
        }
    }

    public void saveAsProcessed(final String eventId) {
        final var sql = "UPDATE events SET processed = true WHERE id = :id";

        if (this.database.update(sql, Map.of("id", eventId)) == 0) {
            throw new RuntimeException();
        }
    }

    private void insertEvent(final Event event) {
        final var sql = """
                INSERT INTO events (processed, aggregate_id, aggregate_type, event_type, event_date, event_data)
                VALUES (:processed, :aggregateId, :aggregateType, :eventType, :eventDate, :eventData)
                """;

        final var params = new HashMap<String, Object>();
        params.put("processed", event.processed());
        params.put("aggregateId", event.aggregateId());
        params.put("aggregateType", event.aggregateType());
        params.put("eventType", event.eventType());
        params.put("eventDate", event.eventDate());
        params.put("eventData", event.eventData());

        try {
            this.database.update(sql, params);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    private static RowMapping<Event> eventRowMapping() {
        return rs -> new Event(
                rs.getLong("event_id"),
                rs.getBoolean("processed"),
                rs.getString("aggregate_id"),
                rs.getString("aggregate_type"),
                rs.getString("event_type"),
                rs.getObject("event_date", Instant.class),
                rs.getBytes("event_data")
        );
    }

    private record Event(
            Long eventId,
            boolean processed,
            String aggregateId,
            String aggregateType,
            String eventType,
            Instant eventDate,
            byte[] eventData
    ) {
        public static Event newEvent(String aggregateId, String aggregateType, String eventType, byte[] eventData) {
            return new Event(null, false, aggregateId, aggregateType, eventType, InstantUtils.now(), eventData);
        }
    }

    private static DomainEvent toDomainEvent(final Event ev) {
        try {
            return (DomainEvent) Json.readValue(ev.eventData(), Class.forName(ev.eventType));
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
