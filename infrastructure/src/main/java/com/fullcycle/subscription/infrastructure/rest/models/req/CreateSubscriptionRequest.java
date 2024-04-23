package com.fullcycle.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(@NotNull Long planId) {

}
