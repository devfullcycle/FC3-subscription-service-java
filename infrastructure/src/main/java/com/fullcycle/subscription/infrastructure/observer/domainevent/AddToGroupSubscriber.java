package com.fullcycle.subscription.infrastructure.observer.domainevent;

import com.fullcycle.subscription.application.account.AddToGroup;
import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.subscription.SubscriptionCreated;
import com.fullcycle.subscription.domain.subscription.SubscriptionRenewed;
import com.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import com.fullcycle.subscription.infrastructure.observer.Subscriber;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AddToGroupSubscriber implements Subscriber<DomainEvent> {

    private final AddToGroup addToGroup;
    private final String subscribersGroup;

    public AddToGroupSubscriber(final AddToGroup addToGroup, final KeycloakProperties keycloakProperties) {
        this.addToGroup = Objects.requireNonNull(addToGroup);
        this.subscribersGroup = Objects.requireNonNull(keycloakProperties).subscribersGroupId();
    }

    @Override
    public boolean test(final DomainEvent ev) {
        return ev instanceof SubscriptionCreated || ev instanceof SubscriptionRenewed;
    }

    @Override
    public void onEvent(final DomainEvent ev) {
        AddToGroupInput in = null;
        if (ev instanceof SubscriptionCreated sc) {
            in = new AddToGroupInput(sc, subscribersGroup);
        } else if (ev instanceof SubscriptionRenewed sc) {
            in = new AddToGroupInput(sc, subscribersGroup);
        } else {
            return;
        }

        this.addToGroup.execute(in);
    }

    record AddToGroupInput(String accountId, String subscriptionId, String groupId) implements AddToGroup.Input {

        public AddToGroupInput(SubscriptionCreated s, String groupId) {
            this(s.accountId(), s.subscriptionId(), groupId);
        }

        public AddToGroupInput(SubscriptionRenewed s, String groupId) {
            this(s.accountId(), s.subscriptionId(), groupId);
        }
    }
}
