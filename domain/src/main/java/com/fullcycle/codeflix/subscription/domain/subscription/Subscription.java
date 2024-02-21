package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.plan.BillingCycle;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationHandler;
import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.fullcycle.codeflix.subscription.domain.utils.InstantUtils.now;

public class Subscription extends AggregateRoot<SubscriptionId> {

    private static final int FIRST_VERSION = 0;

    private UserId userId;
    private int version;
    private Instant createdAt;
    private Instant updatedAt;

    private BillingCycle billingCycle;
    private Double price;
    private boolean active;
    private List<BillingHistory> billingHistories;

    private Subscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final BillingCycle billingCycle,
            final Double price,
            final Boolean active,
            final List<BillingHistory> billingHistories
    ) {
        super(subscriptionId);
        var n = Notification.create();
        setUserId(userId);
        setVersion(version);
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setBillingCycle(billingCycle, n);
        setPrice(price, n);
        setActive(active);
        setBillingHistories(billingHistories);
        n.get("Invalid user subscription");
//        registerEvent(new SubscriptionCreated());
    }

    public static Subscription newSubscription(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final BillingCycle billingCycle,
            final Double price,
            final Boolean active
    ) {
        final var now = now();
        return new Subscription(
                subscriptionId,
                userId,
                FIRST_VERSION,
                now,
                now,
                billingCycle,
                price,
                active,
                Collections.emptyList()
        );
    }

    public static Subscription with(
            final SubscriptionId subscriptionId,
            final UserId userId,
            final int version,
            final Instant createdAt,
            final Instant updatedAt,
            final BillingCycle billingCycle,
            final Double price,
            final Boolean active,
            final List<BillingHistory> billingHistories
    ) {
        return new Subscription(
                subscriptionId,
                userId,
                version,
                createdAt,
                updatedAt,
                billingCycle,
                price,
                active,
                billingHistories
        );
    }

    public Subscription activateSubscription() {
        this.setActive(true);
        this.registerEvent(new SubscriptionActivated(this));
        return this;
    }

    public BillingHistory renewed(final String transactionId) {
        final var record = new BillingHistory(transactionId, now(), this.billingCycle, this.price);
        this.billingHistories.add(record);
        this.registerEvent(new SubscriptionRenewed(this, record));
        return record;
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

    public BillingCycle billingCycle() {
        return billingCycle;
    }

    public Double price() {
        return price;
    }

    public boolean active() {
        return active;
    }

    public List<BillingHistory> billingHistories() {
        return Collections.unmodifiableList(billingHistories);
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    private void setUserId(final UserId userId) {
        this.userId = userId;
    }

    private void setVersion(int version) {
        this.version = version;
    }

    private void setBillingCycle(final BillingCycle billingCycle, final ValidationHandler v) {
        if (billingCycle == null) {
            v.append("'billingCycle' should not be null");
            return;
        }
        this.billingCycle = billingCycle;
    }

    private void setPrice(final Double price, final ValidationHandler v) {
        if (price == null) {
            v.append("'price' should not be null");
            return;
        }
        this.price = price;
    }

    private void setActive(final Boolean active) {
        this.active = active != null && active;
    }

    private void setBillingHistories(final List<BillingHistory> billingHistories) {
        this.billingHistories = billingHistories == null ? new LinkedList<>() : new LinkedList<>(billingHistories);
    }
}
