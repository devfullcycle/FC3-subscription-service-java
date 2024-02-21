package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Plan extends AggregateRoot<PlanId> {

    private String name;
    private String description;
    private boolean active;
    private Set<PricingOption> pricingOptions;

    private Plan(
            final String planId,
            final String name,
            final String description,
            final Boolean active
    ) {
        super(new PlanId(planId));
        setName(name);
        setDescription(description);
        setActive(active);
        setPricingModels(Collections.emptySet());
    }

    public static Plan newPlan(
            final String name,
            final String description,
            final Boolean active
    ) {
        return new Plan(IdUtils.uniqueId(), name, description, active);
    }

    public static Plan with(
            final String planId,
            final String name,
            final String description,
            final Boolean active
    ) {
        return new Plan(planId, name, description, active);
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean active() {
        return active;
    }

    public Set<PricingOption> pricingOptions() {
        return Collections.unmodifiableSet(pricingOptions);
    }

    public Plan addPricingOption(final BillingCycle billingCycle, final Double price, final Boolean active) {
        this.pricingOptions.add(new PricingOption(billingCycle, price, active));
        return this;
    }

    public Plan removePricingOption(final BillingCycle billingCycle, final Double price) {
        this.pricingOptions.removeIf(it -> it.billingCycle() == billingCycle && Objects.equals(it.price(), price));
        return this;
    }

    private void setName(final String name) {
        this.assertArgumentNotEmpty(name, "'name' is required");
        this.assertArgumentMaxLength(name, 255, "'name' must have less than 255 characters");
        this.name = name;
    }

    private void setDescription(final String description) {
        this.assertArgumentNotEmpty(name, "'description' is required");
        this.description = description;
    }

    private void setActive(final Boolean active) {
        this.active = active != null && active;
    }

    private void setPricingModels(final Set<PricingOption> pricingOptions) {
        this.pricingOptions = pricingOptions == null ? new HashSet<>() : new HashSet<>(pricingOptions);
    }

    public boolean hasOption(BillingCycle billingCycle, Double price) {
        return pricingOptions.stream()
                .anyMatch(it -> it.billingCycle() == billingCycle && Objects.equals(it.price(), price));
    }
}
