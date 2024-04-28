package com.fullcycle.subscription.infrastructure.rest.models.req;

import jakarta.validation.constraints.NotBlank;

public record ChargeSubscriptionRequest(
        @NotBlank String paymentType,
        String creditCardToken
) {
}
