package com.fullcycle.codeflix.subscription.domain;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static class Plans {

        public static Plan free() {
            return Plan.newPlan("Free", FAKER.text().text(255), true);
        }
    }
}
