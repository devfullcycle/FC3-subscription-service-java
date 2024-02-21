package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

import java.util.Objects;

public record PricingOption(
        BillingCycle billingCycle,
        Double price,
        Boolean active
) {
    public PricingOption {
        var n = Notification.create();

        if (billingCycle == null) {
            n.append("'billingCycle' should not be null");
        }

        if (price == null) {
            n.append("'price' should not be null");
        }

        if (active == null) {
            n.append("'active' should not be null");
        }

        n.get("Invalid pricing model");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricingOption that = (PricingOption) o;
        return billingCycle == that.billingCycle && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billingCycle, price);
    }
}
