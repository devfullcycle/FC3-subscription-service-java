package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class ActiveSubscriptionException extends DomainException {

    private ActiveSubscriptionException(final String errorMessage) {
        super(errorMessage, List.of(new ValidationError("plan", errorMessage)));
    }

    public ActiveSubscriptionException(final AccountId accountId, final SubscriptionId subscriptionId) {
        this(message(accountId, subscriptionId));
    }

    private static String message(final AccountId accountId, final SubscriptionId subscriptionId) {
        return "User has already an active subscription [userid:%s] [subscriptionid:%s]".formatted(accountId.value(), subscriptionId.value());
    }
}
