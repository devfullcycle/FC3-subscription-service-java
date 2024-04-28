package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.RowMap;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PlanJdbcRepository implements PlanGateway {

    private final DatabaseClient database;

    public PlanJdbcRepository(final DatabaseClient database) {
        this.database = Objects.requireNonNull(database);
    }

    @Override
    public PlanId nextId() {
        return PlanId.empty();
    }

    @Override
    public Optional<Plan> planOfId(PlanId anId) {
        final var sql = "SELECT id, version, name, description, active, currency, amount, created_at, updated_at, deleted_at FROM plans WHERE id = :id";
        return this.database.queryOne(sql, Map.<String, Object>of("id", anId.value()), planMapper());
    }

    @Override
    public List<Plan> allPlans() {
        return List.of();
    }

    @Override
    public boolean existsPlanOfId(PlanId anId) {
        return false;
    }

    @Override
    public Plan save(Plan plan) {
        return null;
    }

    private static RowMap<Plan> planMapper() {
        return rs -> {
            return Plan.with(
                    new PlanId(rs.getLong("id")),
                    rs.getInt("version"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getBoolean("active"),
                    new Money(rs.getString("currency"), rs.getDouble("amount")),
                    rs.getObject("created_at", Instant.class),
                    rs.getObject("updated_at", Instant.class),
                    rs.getObject("deleted_at", Instant.class)
            );
        };
    }
}
