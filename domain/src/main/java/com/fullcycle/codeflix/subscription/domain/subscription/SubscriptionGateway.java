package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.user.UserId;

import java.util.Optional;

public interface SubscriptionGateway {

    SubscriptionId nextId();

    Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId);

    Optional<Subscription> subscriptionOfUser(UserId userId);

    Subscription save(Subscription subscription);

}
