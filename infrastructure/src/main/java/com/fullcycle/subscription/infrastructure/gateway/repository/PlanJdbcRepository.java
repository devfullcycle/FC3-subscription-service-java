package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.plan.Plan;
import com.fullcycle.subscription.domain.plan.PlanGateway;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.RowMap;
import com.fullcycle.subscription.infrastructure.utils.JdbcUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        final var sql = "SELECT id, version, name, description, active, currency, amount, created_at, updated_at, deleted_at FROM plans";
        return this.database.query(sql, planMapper());
    }

    @Override
    public boolean existsPlanOfId(final PlanId anId) {
        final var sql = "SELECT id FROM plans WHERE id = :id";
        return this.database.queryOne(sql, Map.of("id", anId.value()), rs -> rs.getString("id")).isPresent();
    }

    @Override
    @Transactional
    public Plan save(final Plan plan) {
        if (plan.version() == 0) {
            return create(plan);
        } else {
            return update(plan);
        }
    }

    private Plan create(final Plan plan) {
        final var sql = """
                INSERT INTO plans (version, name, description, active, currency, amount, created_at, updated_at, deleted_at)
                VALUES ((:version + 1), :name, :description, :active, :currency, :amount, :createdAt, :updatedAt, :deletedAt)
                """;
        var id = this.database.insert(sql, createParams(plan));
        return plan.withId(new PlanId(id.longValue()));
    }

    private Plan update(final Plan plan) {
        final var sql = """
                UPDATE plans
                SET
                    version = (:version + 1),
                    name = :name,
                    description = :description,
                    active = :active,
                    currency = :currency,
                    amount = :amount,
                    created_at = :createdAt,
                    updated_at = :updatedAt,
                    deleted_at = :deletedAt
                WHERE id = :id and version = :version
                """;

        if (this.database.update(sql, createParams(plan)) == 0) {
            throw new IllegalArgumentException("Plan with id %s and version %s was not found".formatted(plan.id().value(), plan.version()));
        }

        return plan;
    }

    private HashMap<String, Object> createParams(Plan plan) {
        final var params = new HashMap<String, Object>();
        params.put("version", plan.version());
        params.put("name", plan.name());
        params.put("description", plan.description());
        params.put("active", plan.active());
        params.put("currency", plan.price().currency().getCurrencyCode());
        params.put("amount", plan.price().amount());
        params.put("createdAt", plan.createdAt());
        params.put("updatedAt", plan.updatedAt());
        params.put("deletedAt", plan.deletedAt());

        if (!Objects.equals(plan.id(), PlanId.empty())) {
            params.put("id", plan.id().value());
        }

        return params;
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
                    JdbcUtil.getInstant(rs, "created_at"),
                    JdbcUtil.getInstant(rs, "updated_at"),
                    JdbcUtil.getInstant(rs, "deleted_at")
            );
        };
    }
}
