package com.fullcycle.subscription.domain.subscription;

import com.fullcycle.subscription.domain.AssertionConcern;

public sealed interface SubscriptionCommand extends AssertionConcern {

    record IncompleteSubscription(String aReason, String aTransactionId) implements SubscriptionCommand {
        public IncompleteSubscription {
            this.assertArgumentNotEmpty(aTransactionId, "'transactionId' should not be empty");
        }
    }

    record ChangeStatus(String status) implements SubscriptionCommand {
        public ChangeStatus {
            this.assertArgumentNotEmpty(status, "'status' should not be empty");
        }
    }
}
