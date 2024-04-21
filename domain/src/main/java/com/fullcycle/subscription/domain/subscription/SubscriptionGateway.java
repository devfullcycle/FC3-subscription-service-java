package com.fullcycle.subscription.domain.subscription;

import java.util.Optional;

public interface SubscriptionGateway {

    Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId);

    Subscription save(Subscription subscription);
}
