package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.status.SubscriptionStatus;
import com.fullcycle.codeflix.subscription.domain.subscription.status.SubscriptionStatusFactory;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

import static com.fullcycle.codeflix.subscription.domain.utils.InstantUtils.now;

public class Subscription extends AggregateRoot<SubscriptionId> {

    private static final int FIRST_VERSION = 0;
    public static final int TRIAL_MONTHS = 1;

    private PlanId planId;
    private UserId userId;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDate dueDate;
    private SubscriptionStatus status;
    private Instant lastRenewDate;
    private String lastTransactionId;

    private Subscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final String status,
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
        setStatus(SubscriptionStatusFactory.create(this, status));
        setDueDate(dueDate);
        setLastRenewDate(lastRenewDate);
        setLastTransactionId(lastTransactionId);
    }

    public static Subscription newSubscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId
    ) {
        final var now = now();
        return new Subscription(
                subscriptionId,
                userId,
                planId,
                FIRST_VERSION,
                now,
                now,
                "trailing",
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
            final String status,
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
                status,
                dueDate,
                lastRenewDate,
                lastTransactionId
        );
    }

    public Subscription incomplete(final String reason) {
        this.assertArgumentNotEmpty(reason, "'reason' should not be empty");
        this.status.incomplete();
        this.registerEvent(new SubscriptionIncomplete(this, reason));
        return this;
    }

    public Subscription renew(final Plan plan, final String transactionId) {
        this.assertArgumentNotEmpty(transactionId, "'transactionId' should not be empty");
        this.status.active();
        this.setDueDate((dueDate != null ? dueDate : LocalDate.now()).plusMonths(1));
        this.setLastRenewDate(Instant.now());
        this.setLastTransactionId(transactionId);
        this.setUpdatedAt(InstantUtils.now());
        this.registerEvent(new SubscriptionRenewed(this, plan, transactionId));
        return this;
    }

    public Subscription canceled() {
        this.status.canceled();
        this.setUpdatedAt(InstantUtils.now());
        this.registerEvent(new SubscriptionCanceled(this));
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

    public SubscriptionStatus status() {
        return status;
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

    public void setStatus(final SubscriptionStatus status) {
        this.assertArgumentNotNull(status, "'status' is required");
        this.status = status;
    }

    private void setCreatedAt(final Instant createdAt) {
        this.assertArgumentNotNull(createdAt, "'createdAt' is required");
        this.createdAt = createdAt;
    }

    private void setUpdatedAt(final Instant updatedAt) {
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
