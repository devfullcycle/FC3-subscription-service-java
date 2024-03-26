package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.monetary.MonetaryAmount;

public class Plan extends AggregateRoot<PlanId> {

    private String name;
    private String description;
    private String groupId;
    private boolean active;
    private MonetaryAmount price;

    private Plan(
            final PlanId planId,
            final String name,
            final String description,
            final String groupId,
            final Boolean active,
            final String currency,
            final Double price
    ) {
        super(planId);
        setName(name);
        setDescription(description);
        setGroupId(groupId);
        setActive(active);
        setPrice(new MonetaryAmount(price, currency));
    }

    public static Plan with(
            final PlanId planId,
            final String name,
            final String description,
            final String groupId,
            final Boolean active,
            final String currency,
            final Double price
    ) {
        return new Plan(planId, name, description, groupId, active, currency, price);
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean active() {
        return active;
    }

    public MonetaryAmount price() {
        return price;
    }

    public String groupId() {
        return groupId;
    }

    private void setName(final String name) {
        this.assertArgumentNotEmpty(name, "'name' is required");
        this.assertArgumentMaxLength(name, 255, "'name' must have less than 255 characters");
        this.name = name;
    }

    private void setDescription(final String description) {
        this.assertArgumentNotEmpty(name, "'description' is required");
        this.description = description;
    }

    private void setActive(final Boolean active) {
        this.active = active != null && active;
    }

    private void setGroupId(final String groupId) {
        this.assertArgumentNotEmpty(groupId, "'groupId' is required");
        this.groupId = groupId;
    }

    private void setPrice(final MonetaryAmount price) {
        this.assertArgumentNotNull(price, "'price' should not be null");
        this.price = price;
    }
}
