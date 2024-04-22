package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.plan.PlanId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PlanInMemoryRepository implements PlanGateway {

    private static final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Plan> db = new ConcurrentHashMap<>();

    @Override
    public PlanId nextId() {
        return new PlanId(id.getAndIncrement());
    }

    @Override
    public Optional<Plan> planOfId(PlanId anId) {
        return Optional.ofNullable(this.db.get(anId.value()));
    }

    @Override
    public List<Plan> allPlans() {
        return this.db.values().stream().toList();
    }

    @Override
    public boolean existsPlanOfId(PlanId anId) {
        return this.db.containsKey(anId.value());
    }

    @Override
    public Plan save(Plan plan) {
        this.db.put(plan.id().value(), plan);
        return plan;
    }
}
