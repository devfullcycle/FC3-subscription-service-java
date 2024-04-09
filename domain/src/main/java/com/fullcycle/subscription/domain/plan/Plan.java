package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.AggregateRoot;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.utils.InstantUtils;

import java.time.Instant;

public class Plan extends AggregateRoot<PlanId> {

    private int version;
    private String name;
    private String description;
    private boolean active;
    private Money price;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Plan(
            final PlanId planId,
            final int aVersion,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(planId);
        this.setVersion(aVersion);
        this.setName(aName);
        this.setDescription(aDescription);
        this.setActive(anActive);
        this.setPrice(aPrice);
        this.setCreatedAt(createdAt);
        this.setUpdatedAt(updatedAt);
        this.setDeletedAt(deletedAt);
    }

    public static Plan newPlan(
            final PlanId planId,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice
    ) {
        final var now = InstantUtils.now();
        final var isActive = anActive != null ? anActive : false;
        return new Plan(planId, 0, aName, aDescription, isActive, aPrice, now, now, isActive ? null : now);
    }

    public static Plan with(
            final PlanId planId,
            final int aVersion,
            final String aName,
            final String aDescription,
            final Boolean anActive,
            final Money aPrice,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Plan(planId, aVersion, aName, aDescription, anActive, aPrice, createdAt, updatedAt, deletedAt);
    }

    public void execute(final PlanCommand... cmds) {
        if (cmds == null) {
            return;
        }

        for (var cmd : cmds) {
            switch (cmd) {
                case PlanCommand.ActivatePlan c -> apply(c);
                case PlanCommand.ChangePlan c -> apply(c);
                case PlanCommand.InactivatePlan c -> apply(c);
            }
        }

        this.setVersion(version() + 1);
        this.setUpdatedAt(InstantUtils.now());
    }

    private void apply(final PlanCommand.ActivatePlan cmd) {
        this.setDeletedAt(null);
        this.setActive(true);
    }

    private void apply(final PlanCommand.ChangePlan cmd) {
        this.setName(cmd.name());
        this.setDescription(cmd.description());
        if (Boolean.TRUE.equals(cmd.active())) {
            apply(new PlanCommand.ActivatePlan());
        } else {
            apply(new PlanCommand.InactivatePlan());
        }
    }

    private void apply(final PlanCommand.InactivatePlan cmd) {
        this.setDeletedAt(deletedAt != null ? deletedAt : InstantUtils.now());
        this.setActive(false);
    }

    public int version() {
        return version;
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

    public Money price() {
        return price;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    private void setVersion(final int version) {
        this.version = version;
    }

    private void setName(final String name) {
        this.assertArgumentNotEmpty(name, "'name' should not be empty");
        this.assertArgumentMaxLength(name, 100, "'name' should have more than 100 characters");
        this.name = name;
    }

    private void setDescription(final String description) {
        this.assertArgumentNotEmpty(description, "'description' should not be empty");
        this.assertArgumentMaxLength(description, 500, "'description' should have more than 500 characters");
        this.description = description;
    }

    private void setActive(final Boolean active) {
        this.assertArgumentNotNull(active, "'active' should not be null");
        this.active = active;
    }

    private void setPrice(final Money price) {
        this.assertArgumentNotNull(price, "'price' should not be null");
        this.price = price;
    }

    private void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    private void setUpdatedAt(final Instant updatedAt) {
        this.assertArgumentNotNull(updatedAt, "'updatedAt' should not be null");
        this.updatedAt = updatedAt;
    }

    private void setCreatedAt(final Instant createdAt) {
        this.assertArgumentNotNull(createdAt, "'createdAt' should not be null");
        this.createdAt = createdAt;
    }
}
