package com.fullcycle.codeflix.subscription.domain;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static class Plans {

        public static Plan free() {
            return Plan.with(
                    new PlanId(IdUtils.uniqueId()), 0, "Free", FAKER.text().text(255),
                    "free", true, "USD", 0.0
            );
        }
    }
}
