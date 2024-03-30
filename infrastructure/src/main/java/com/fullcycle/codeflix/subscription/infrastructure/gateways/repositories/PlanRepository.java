package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.domain.plan.Plan;
import com.fullcycle.codeflix.subscription.domain.plan.PlanGateway;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.database.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PlanRepository implements PlanGateway {

    private final DatabaseClient database;

    public PlanRepository(final DatabaseClient database) {
        this.database = Objects.requireNonNull(database);
    }

    @Override
    public PlanId nextId() {
        return new PlanId(IdUtils.uniqueId());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Plan> allPlans() {
        final var sql = "SELECT id, version, name, description, group_id, active, currency, amount FROM plans";
        return this.database.query(sql, Map.of(), rs -> Plan.with(
                new PlanId(rs.getString(1)),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getBoolean(6),
                rs.getString(7),
                rs.getDouble(8)
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Plan> planOfId(final PlanId planId) {
        final var sql = "SELECT id, version, name, description, group_id, active, currency, amount FROM plans WHERE id = :id";
        final var rows = this.database.query(sql, Map.of("id", planId.value()), rs -> Plan.with(
                new PlanId(rs.getString(1)),
                rs.getInt(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getBoolean(6),
                rs.getString(7),
                rs.getDouble(8)
        ));
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPlanOfId(PlanId planId) {
        final var sql = "SELECT id FROM plans WHERE id = :id";
        final var rows = this.database.query(sql, Map.of("id", planId.value()), rs -> rs.getString(1));
        return rows.isEmpty();
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
                INSERT INTO plans (id, version, name, description, group_id, active, currency, amount)
                VALUES (:id, :version, :name, :description, :groupId, :active, :currency, :amount)
                """;
        executeUpdate(sql, plan);
        return plan;
    }

    private Plan update(final Plan plan) {
        final var sql = """
                UPDATE plans
                SET
                    version = :version,
                    name = :name,
                    description = :description,
                    group_id = :groupId,
                    active = :active,
                    currency = :currency,
                    amount = :amount
                FROM Plan
                WHERE id = :id AND version = (:version - 1)
                """;

        if (executeUpdate(sql, plan) == 0) {
            throw new IllegalStateException("0 rows affected");
        }

        return plan;
    }

    private int executeUpdate(final String sql, final Plan plan) {
        final var params = Map.<String, Object>of(
                "id", plan.id().value(),
                "version", plan.version(),
                "name", plan.name(),
                "description", plan.description(),
                "groupId", plan.groupId(),
                "active", plan.active(),
                "currency", plan.price().currency().getCurrencyCode(),
                "amount", plan.price().amount()
        );

        return this.database.update(sql, params);
    }
}
