package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.subscription.status.SubscriptionStatus;

public sealed interface SubscriptionCommand {

    record ChangeStatus(SubscriptionStatus status) implements SubscriptionCommand {

    }
}
