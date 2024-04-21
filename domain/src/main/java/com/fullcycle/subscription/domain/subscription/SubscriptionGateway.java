package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.account.AccountId;

import java.util.Optional;

public interface SubscriptionGateway {

    Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId);

    Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId);

    Subscription save(Subscription subscription);

    SubscriptionId nextId();

}
