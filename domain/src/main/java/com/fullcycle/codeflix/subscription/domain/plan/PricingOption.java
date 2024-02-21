package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.ValueObject;

import java.util.Objects;

public record PricingOption(BillingCycle billingCycle, Double price, Boolean active) implements ValueObject {
    public PricingOption {
        this.assertArgumentNotNull(billingCycle, "'billingCycle' is required");
        this.assertArgumentNotNull(price, "'price' is required");
        this.assertArgumentNotNull(active, "'active' is required");
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
