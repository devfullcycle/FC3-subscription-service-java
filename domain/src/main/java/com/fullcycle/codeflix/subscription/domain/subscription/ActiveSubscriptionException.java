package com.fullcycle.codeflix.subscription.domain.subscription;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.user.UserId;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class ActiveSubscriptionException extends DomainException {

    private ActiveSubscriptionException(final String errorMessage) {
        super(errorMessage, List.of(new ValidationError("plan", errorMessage)));
    }

    public ActiveSubscriptionException(final UserId userId, final SubscriptionId subscriptionId) {
        this(message(userId, subscriptionId));
    }

    private static String message(final UserId userId, final SubscriptionId subscriptionId) {
        return "User has already an active subscription [userid:%s] [subscriptionid:%s]".formatted(userId.value(), subscriptionId.value());
    }
}
