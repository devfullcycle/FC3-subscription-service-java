package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.billing.BillingDetailsId;
import com.fullcycle.codeflix.subscription.domain.subscription.billing.BillingRecord;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.fullcycle.codeflix.subscription.domain.utils.InstantUtils.now;

public class Subscription extends AggregateRoot<SubscriptionId> {

    private static final int FIRST_VERSION = 0;

    private PlanId planId;
    private UserId userId;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDate dueDate;
    private boolean active;
    private Instant lastRenewDate;
    private String lastTransactionId;

    private Subscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final Boolean active,
            final LocalDate dueDate,
            final Instant lastRenewDate,
            final String lastTransactionId
    ) {
        super(subscriptionId);
        setUserId(userId);
        setPlanId(planId);
        setVersion(version);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setActive(active);
        setDueDate(dueDate);
        setLastRenewDate(lastRenewDate);
        setLastTransactionId(lastTransactionId);
    }

    public static Subscription newSubscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final Boolean active
    ) {
        final var now = now();
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                FIRST_VERSION,
                now,
                now,
                active,
                null,
                null,
                null
        );
    }

    public static Subscription with(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final Boolean active,
            final LocalDate dueDate,
            final Instant lastRenewDate,
            final String lastTransactionId
    ) {
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                version,
                createdAt,
                updatedAt,
                active,
                dueDate,
                lastRenewDate,
                lastTransactionId
        );
    }

    public Subscription activate() {
        this.setActive(true);
        this.setDueDate(LocalDate.now().minusDays(1));
        this.registerEvent(new SubscriptionActivated(this));
        return this;
    }

    public Subscription renewed(final Plan plan, final String transactionId) {
        this.assertArgumentNotEmpty(transactionId, "'transactionId' should not be empty");

        this.setDueDate((dueDate != null ? dueDate : LocalDate.now()).plusMonths(1));
        this.setLastRenewDate(Instant.now());
        this.setLastTransactionId(transactionId);
        this.registerEvent(new SubscriptionRenewed(this, plan, transactionId));
        return this;
    }

    public UserId userId() {
        return userId;
    }

    public int version() {
        return version;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public boolean active() {
        return active;
    }

    public PlanId planId() {
        return planId;
    }

    public LocalDate dueDate() {
        return dueDate;
    }

    public Instant lastRenewDate() {
        return lastRenewDate;
    }

    public String lastTransactionId() {
        return lastTransactionId;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.assertArgumentNotNull(createdAt, "'createdAt' is required");
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.assertArgumentNotNull(updatedAt, "'updatedAt' is required");
        this.updatedAt = updatedAt;
    }

    private void setUserId(final UserId userId) {
        this.assertArgumentNotNull(userId, "'userId' is required");
        this.userId = userId;
    }

    private void setVersion(int version) {
        this.assertArgumentNotNull(version, "'version' is required");
        this.version = version;
    }

    private void setActive(final Boolean active) {
        this.active = active != null && active;
    }

    private void setPlanId(final PlanId planId) {
        this.assertArgumentNotNull(planId, "'plan' is required");
        this.planId = planId;
    }

    private void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    private void setLastRenewDate(Instant lastRenewDate) {
        this.lastRenewDate = lastRenewDate;
    }

    private void setLastTransactionId(String lastTransactionId) {
        this.lastTransactionId = lastTransactionId;
    }
}
