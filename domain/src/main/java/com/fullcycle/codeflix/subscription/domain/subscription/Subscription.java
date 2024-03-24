package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.user.UserId;

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
    private Status status;
    private Instant lastRenewDate;
    private String lastTransactionId;

    private Subscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final PlanId planId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final Status status,
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
        setStatus(status);
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
                Status.PENDING_PAYMENT,
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
            final Status status,
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

    public Subscription activate() {
        if (this.dueDate == null) {
            setDueDate(LocalDate.now().plusMonths(TRIAL_MONTHS));
        }
        this.setStatus(Status.PENDING_PAYMENT);
        this.registerEvent(new SubscriptionActivated(this));
        return this;
    }

    public Subscription renew(final Plan plan, final String transactionId) {
        this.assertArgumentNotEmpty(transactionId, "'transactionId' should not be empty");
        this.setStatus(Status.PAID);
        this.setDueDate((dueDate != null ? dueDate : LocalDate.now()).plusMonths(1));
        this.setLastRenewDate(Instant.now());
        this.setLastTransactionId(transactionId);
        this.registerEvent(new SubscriptionRenewed(this, plan, transactionId));
        return this;
    }

    public Subscription active() {
        this.setStatus(Status.ACTIVE);
        this.registerEvent(new SubscriptionActivated(this));
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

    public Status status() {
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

    private void setPlanId(final PlanId planId) {
        this.assertArgumentNotNull(planId, "'plan' is required");
        this.planId = planId;
    }

    public void setStatus(final Status status) {
        this.assertArgumentNotNull(status, "'status' is required");
        this.status = status;
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
