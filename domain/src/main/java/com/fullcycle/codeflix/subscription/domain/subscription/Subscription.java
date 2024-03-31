package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.status.*;
import com.fullcycle.codeflix.subscription.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.LocalDate;

import static com.fullcycle.codeflix.subscription.domain.utils.InstantUtils.now;

public class Subscription extends AggregateRoot<SubscriptionId> {

    private static final int FIRST_VERSION = 0;
    private static final int TRIAL_MONTHS = 1;

    private int version;
    private AccountId accountId;
    private PlanId planId;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDate dueDate;
    private SubscriptionStatus status;
    private Instant lastRenewDate;
    private String lastTransactionId;

    private Subscription(
            final SubscriptionId subscriptionId,
            final AccountId accountId,
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
        setAccountId(accountId);
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
            final AccountId accountId,
            final Plan plan
    ) {
        final var now = now();
        final var subscription = new Subscription(
                subscriptionId,
                accountId,
                plan.id(),
                FIRST_VERSION,
                now,
                now,
                SubscriptionStatus.TRAILING,
                LocalDate.now().plusMonths(TRIAL_MONTHS),
                null,
                null
        );
        subscription.registerEvent(new SubscriptionCreated(subscription, plan));
        return subscription;
    }

    public static Subscription with(
            final SubscriptionId subscriptionId,
            final int version,
            final AccountId accountId,
            final PlanId planId,
            final Instant createdAt,
            final Instant updatedAt,
            final String status,
            final LocalDate dueDate,
            final Instant lastRenewDate,
            final String lastTransactionId
    ) {
        return new Subscription(
                subscriptionId,
                accountId,
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

    public Subscription incomplete(final String reason, final String lastTransactionId) {
        this.assertArgumentNotEmpty(reason, "'reason' should not be empty");
        this.status.incomplete();
        this.setLastTransactionId(lastTransactionId);
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

    public Subscription cancel() {
        this.status.cancel();
        this.setUpdatedAt(InstantUtils.now());
        this.registerEvent(new SubscriptionCanceled(this));
        return this;
    }

    public AccountId accountId() {
        return accountId;
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

    public boolean isCanceled() {
        return this.status instanceof CanceledSubscriptionStatus;
    }

    public boolean isTrail() {
        return status instanceof TrailingSubscriptionStatus;
    }

    public boolean isActive() {
        return status instanceof ActiveSubscriptionStatus;
    }

    public boolean isIncomplete() {
        return status instanceof IncompleteSubscriptionStatus;
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

    private void setAccountId(final AccountId accountId) {
        this.assertArgumentNotNull(accountId, "'accountId' is required");
        this.accountId = accountId;
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
