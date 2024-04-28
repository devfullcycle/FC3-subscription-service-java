package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.utils.InstantUtils;
import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.RowMap;
import com.fullcycle.subscription.infrastructure.json.Json;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public class EventJdbcRepository {

    private final DatabaseClient database;

    public EventJdbcRepository(final DatabaseClient databaseClient) {
        this.database = Objects.requireNonNull(databaseClient);
    }

    public List<DomainEvent> allEventsOfAggregate(final String aggregateId, final String aggregateType) {
        final var sql = "SELECT event_id, processed, aggregate_id, aggregate_type, event_type, event_date, event_data FROM events WHERE aggregate_id = :aggregateId and aggregate_type = :aggregateType";
        final var params = Map.<String, Object>of("aggregateId", aggregateId, "aggregateType", aggregateType);
        return this.database.query(sql, params, eventMapper()).stream()
                .map(this::toDomainEvent)
                .toList();
    }

    public void saveAll(final Collection<DomainEvent> events) {
        for (var ev : events) {
            this.insertEvent(Event.newEvent(ev.aggregateId(), ev.aggregateType(), ev.getClass().getCanonicalName(), Json.writeValueAsString(ev)));
        }
    }

    private void insertEvent(final Event event) {
        final var sql = "INSERT INTO events (processed, aggregate_id, aggregate_type, event_type, event_date, event_data) VALUES (:processed, :aggregateId, :aggregateType, :eventType, :eventDate, :eventData)";

        final var params = new HashMap<String, Object>();
        params.put("processed", event.processed());
        params.put("aggregateId", event.aggregateId());
        params.put("aggregateType", event.aggregateType());
        params.put("eventType", event.eventType());
        params.put("eventDate", event.eventDate());
        params.put("eventData", event.eventData());

        this.database.update(sql, params);
    }

    private DomainEvent toDomainEvent(final Event event) {
        try {
            return (DomainEvent) Json.readTree(event.eventData(), Class.forName(event.eventType()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMap<Event> eventMapper() {
        return (rs) -> {
            return new Event(
                    rs.getLong("event_id"),
                    rs.getBoolean("processed"),
                    rs.getString("aggregate_id"),
                    rs.getString("aggregate_type"),
                    rs.getString("event_type"),
                    rs.getTimestamp("event_date").toInstant(),
                    rs.getString("event_data")
            );
        };
    }

    private record Event(
            Long eventId,
            boolean processed,
            String aggregateId,
            String aggregateType,
            String eventType,
            Instant eventDate,
            String eventData
    ) {

        public static Event newEvent(String aggregateId, String aggregateType, String eventType, String data) {
            return new Event(null, false, aggregateId, aggregateType, eventType, InstantUtils.now(), data);
        }
    }
}
