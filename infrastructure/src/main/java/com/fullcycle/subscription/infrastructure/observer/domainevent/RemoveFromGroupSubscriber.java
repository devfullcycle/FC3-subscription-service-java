package com.fullcycle.subscription.infrastructure.observer.domainevent;

import com.fullcycle.subscription.application.account.RemoveFromGroup;
import com.fullcycle.subscription.domain.DomainEvent;
import com.fullcycle.subscription.domain.subscription.SubscriptionCanceled;
import com.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import com.fullcycle.subscription.infrastructure.observer.Subscriber;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RemoveFromGroupSubscriber implements Subscriber<DomainEvent> {

    private final RemoveFromGroup removeFromGroup;
    private final String subscribersGroup;

    public RemoveFromGroupSubscriber(final RemoveFromGroup removeFromGroup, final KeycloakProperties keycloakProperties) {
        this.removeFromGroup = Objects.requireNonNull(removeFromGroup);
        this.subscribersGroup = Objects.requireNonNull(keycloakProperties).subscribersGroupId();
    }

    @Override
    public boolean test(final DomainEvent ev) {
        return ev instanceof SubscriptionCanceled;
    }

    @Override
    public void onEvent(final DomainEvent ev) {
        RemoveFromGroupInput in = null;
        if (ev instanceof SubscriptionCanceled sc) {
            in = new RemoveFromGroupInput(sc, subscribersGroup);
        } else {
            return;
        }

        this.removeFromGroup.execute(in);
    }

    record RemoveFromGroupInput(String accountId, String subscriptionId, String groupId) implements RemoveFromGroup.Input {

        public RemoveFromGroupInput(SubscriptionCanceled s, String groupId) {
            this(s.accountId(), s.subscriptionId(), groupId);
        }
    }
}
