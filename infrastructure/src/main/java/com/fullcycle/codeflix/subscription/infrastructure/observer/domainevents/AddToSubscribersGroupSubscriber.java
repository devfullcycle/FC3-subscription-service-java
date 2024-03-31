package com.fullcycle.codeflix.subscription.infrastructure.observer.domainevents;

import com.fullcycle.codeflix.subscription.application.account.AddToSubscribersGroup;
import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionCreated;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionRenewed;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class AddToSubscribersGroupSubscriber implements Subscriber<DomainEvent> {

    private final AddToSubscribersGroup addToSubscribersGroup;

    public AddToSubscribersGroupSubscriber(final AddToSubscribersGroup addToSubscribersGroup) {
        this.addToSubscribersGroup = addToSubscribersGroup;
    }

    @Override
    public boolean test(final DomainEvent ev) {
        return ev instanceof SubscriptionCreated || ev instanceof SubscriptionRenewed;
    }

    @Override
    public void onEvent(DomainEvent event) {
        AddToSubscribersGroupInput in = null;
        if (event instanceof SubscriptionCreated ev) {
            in = new AddToSubscribersGroupInput(ev);
        } else if (event instanceof SubscriptionRenewed ev) {
            in = new AddToSubscribersGroupInput(ev);
        } else {
            return;
        }

        this.addToSubscribersGroup.execute(in);
    }

    record AddToSubscribersGroupInput(String accountId, String subscriptionId, String groupId) implements AddToSubscribersGroup.Input {
        public AddToSubscribersGroupInput(SubscriptionCreated ev) {
            this(ev.accountId(), ev.subscriptionId(), ev.groupId());
        }

        public AddToSubscribersGroupInput(SubscriptionRenewed ev) {
            this(ev.accountId(), ev.subscriptionId(), ev.groupId());
        }
    }
}
