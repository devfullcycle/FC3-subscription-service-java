package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.account.AccountId;

import java.util.Optional;

public interface SubscriptionGateway {

    SubscriptionId nextId();

    Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId);

    Optional<Subscription> latestSubscriptionOfUser(AccountId accountId);

    void save(Subscription subscription);

}
