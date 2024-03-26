package com.fullcycle.codeflix.subscription.domain.plan;

import com.fullcycle.codeflix.subscription.domain.exceptions.DomainException;
import com.fullcycle.codeflix.subscription.domain.validation.ValidationError;

import java.util.List;

public class PlanNotFoundException extends DomainException {

    private PlanNotFoundException(final String errorMessage) {
        super(errorMessage, List.of(new ValidationError("plan", errorMessage)));
    }

    public PlanNotFoundException(final PlanId planId) {
        this(message(planId));
    }

    private static String message(final PlanId planId) {
        return "Plan not found [planid:%s]".formatted(planId.value());
    }
}
