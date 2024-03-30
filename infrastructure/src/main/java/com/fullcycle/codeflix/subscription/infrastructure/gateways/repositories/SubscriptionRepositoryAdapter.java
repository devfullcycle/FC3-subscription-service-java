package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;

import java.util.Objects;
import java.util.Optional;

/**
 * Esse é um caso particular onde utilizamos de componentes do framework para abstrair
 * nossa camada de persistência e ele já nos obriga a utilizar o Repository Pattern.
 *
 * Nesse caso, como já temos que criar um SubscriptionRepository onde o mesmo é implementado pelo framework
 * nosso papel aqui é ser apenas uma Adapter do repositório para o Gateway.
 */
public class SubscriptionRepositoryAdapter implements SubscriptionGateway {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionRepositoryAdapter(final SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = Objects.requireNonNull(subscriptionRepository);
    }

    @Override
    public SubscriptionId nextId() {
        return null;
    }

    @Override
    public Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId) {
        return Optional.empty();
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfUser(AccountId accountId) {
        return Optional.empty();
    }

    @Override
    public Subscription save(Subscription subscription) {
        return null;
    }
}
