package com.fullcycle.codeflix.subscription.infrastructure.observer.domainevents;

import com.fullcycle.codeflix.subscription.application.account.RemoveFromSubscribersGroup;
import com.fullcycle.codeflix.subscription.domain.DomainEvent;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionCanceled;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionIncomplete;
import com.fullcycle.codeflix.subscription.infrastructure.observer.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class RemoveFromSubscribersGroupSubscriber implements Subscriber<DomainEvent> {

    private final RemoveFromSubscribersGroup removeFromSubscribersGroup;

    public RemoveFromSubscribersGroupSubscriber(RemoveFromSubscribersGroup removeFromSubscribersGroup) {
        this.removeFromSubscribersGroup = removeFromSubscribersGroup;
    }

    @Override
    public boolean test(final DomainEvent ev) {
        return ev instanceof SubscriptionCanceled || ev instanceof SubscriptionIncomplete;
    }

    @Override
    public void onEvent(DomainEvent event) {
        RemoveFromSubscribersGroupInput in;
        if (event instanceof SubscriptionCanceled ev) {
            in = new RemoveFromSubscribersGroupInput(ev);
        } else if (event instanceof SubscriptionIncomplete ev) {
            in = new RemoveFromSubscribersGroupInput(ev);
        } else {
            return;
        }
        this.removeFromSubscribersGroup.execute(in);
    }

    record RemoveFromSubscribersGroupInput(String accountId, String subscriptionId) implements RemoveFromSubscribersGroup.Input {
        public RemoveFromSubscribersGroupInput(SubscriptionCanceled ev) {
            this(ev.accountId(), ev.subscriptionId());
        }

        public RemoveFromSubscribersGroupInput(SubscriptionIncomplete ev) {
            this(ev.accountId(), ev.subscriptionId());
        }
    }
}
