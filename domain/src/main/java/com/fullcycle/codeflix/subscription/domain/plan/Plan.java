package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.AggregateRoot;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationHandler;
import com.fullcycle.codeflix.subscription.domain.validation.handler.Notification;

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
        var n = Notification.create();
        setName(name, n);
        setDescription(description, n);
        setActive(active);
        setPricingModels(Collections.emptySet());
        n.get("Invalid Plan");
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

    public ValidationHandler addPricingOption(final BillingCycle billingCycle, final Double price, final Boolean active) {
        var n = Notification.create();
        n.validate(() -> new PricingOption(billingCycle, price, active));
        return n;
    }

    public void removePricingOption(final BillingCycle billingCycle, final Double price) {
        this.pricingOptions.removeIf(it -> it.billingCycle() == billingCycle && Objects.equals(it.price(), price));
    }

    private void setName(final String name, final ValidationHandler v) {
        if (name == null || name.isBlank()) {
            v.append("'name' should not be empty");
            return;
        }
        this.name = name;
    }

    private void setDescription(final String description, final ValidationHandler v) {
        if (description == null || description.isBlank()) {
            v.append("'description' should not be empty");
            return;
        }
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
