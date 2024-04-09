package com.fullcycle.subscription.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> implements AssertionConcern {

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id) {
        this(id, null);
    }

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        this.id = this.assertArgumentNotNull(id, "'id' should not be null");
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public ID id() {
        return id;
    }

    public List<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void registerEvent(final DomainEvent event) {
        if (event == null) {
            return;
        }

        this.domainEvents.add(event);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return id().equals(entity.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
