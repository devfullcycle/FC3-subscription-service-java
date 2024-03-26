package com.fullcycle.codeflix.subscription.infrastructure.gateways;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.infrastructure.repositories.PlanRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Esse é um caso particular onde utilizamos de componentes do framework para abstrair
 * nossa camada de persistência e ele já nos obriga a utilizar o Repository Pattern.
 *
 * Nesse caso, como já temos que criar um PlanRepository onde o mesmo é implementado pelo framework
 * nosso papel aqui é ser apenas uma Adapter do repositório para o Gateway.
 */
public class PlanRepositoryAdapter implements PlanGateway {

    private final PlanRepository planRepository;

    public PlanRepositoryAdapter(final PlanRepository planRepository) {
        this.planRepository = Objects.requireNonNull(planRepository);
    }

    @Override
    public PlanId nextId() {
        return null;
    }

    @Override
    public Collection<Plan> allPlans() {
        return null;
    }

    @Override
    public Optional<Plan> planOfId(PlanId planId) {
        return Optional.empty();
    }

    @Override
    public boolean existsPlanOfId(PlanId planId) {
        return false;
    }

    @Override
    public Plan save(Plan subscription) {
        return null;
    }
}
