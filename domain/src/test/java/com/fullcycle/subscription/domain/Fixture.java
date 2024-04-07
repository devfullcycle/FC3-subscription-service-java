package com.fullcycle.subscription.domain;

import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanId;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static final class Plans {

        public static Plan plus() {
            return Plan.newPlan(
                    new PlanId(123L),
                    "Plus",
                    FAKER.text().text(100, 500),
                    true,
                    new Money("BRL", 20D)
            );
        }
    }
}
