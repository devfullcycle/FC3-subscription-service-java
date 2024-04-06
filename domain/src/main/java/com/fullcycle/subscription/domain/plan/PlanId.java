package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.Identifier;
import com.fullcycle.subscription.domain.exceptions.DomainException;

import java.util.Objects;

public final class PlanId implements Identifier<Long> {

    private final Long value;

    private PlanId() {
        this.value = null;
    }

    public PlanId(final Long value) {
        if (value == null) {
            throw DomainException.with("'planId' should not be null");
        }
        this.value = value;
    }

    public static PlanId empty() {
        return new PlanId();
    }

    @Override
    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PlanId) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PlanId[" +
                "value=" + value + ']';
    }

}
