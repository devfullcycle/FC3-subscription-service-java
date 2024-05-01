package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.domain.utils.IdUtils;
import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.RowMap;
import com.fullcycle.subscription.infrastructure.utils.JdbcUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class SubscriptionJdbcRepository implements SubscriptionGateway {

    private final DatabaseClient database;
    private final EventJdbcRepository eventJdbcRepository;

    public SubscriptionJdbcRepository(final DatabaseClient databaseClient, final EventJdbcRepository eventJdbcRepository) {
        this.database = Objects.requireNonNull(databaseClient);
        this.eventJdbcRepository = Objects.requireNonNull(eventJdbcRepository);
    }

    @Override
    public SubscriptionId nextId() {
        return new SubscriptionId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(final AccountId accountId) {
        final var sql = "SELECT id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id FROM subscriptions WHERE account_id = :accountId";
        return this.database.queryOne(sql, Map.of("accountId", accountId.value()), subscriptionMapper());
    }

    @Override
    public Optional<Subscription> subscriptionOfId(final SubscriptionId subscriptionId) {
        final var sql = "SELECT id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id FROM subscriptions WHERE id = :id";
        return this.database.queryOne(sql, Map.of("id", subscriptionId.value()), subscriptionMapper());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Subscription save(final Subscription subscription) {
        if (subscription.version() == 0) {
            create(subscription);
        } else {
            update(subscription);
        }

        this.eventJdbcRepository.saveAll(subscription.domainEvents());
        return subscription;
    }

    private void create(final Subscription subscription) {
        final var sql = """
                INSERT INTO subscriptions (id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id)
                VALUES (:id, (:version + 1), :accountId, :planId, :status, :createdAt, :updatedAt, :dueDate, :lastRenewDt, :lastTransactionId)
                """;
        executeUpdate(sql, subscription);
    }

    private void update(final Subscription subscription) {
        final var sql = """
                UPDATE subscriptions
                SET
                    version = (:version + 1),
                    account_id = :accountId,
                    plan_id = :planId,
                    status = :status,
                    created_at = :createdAt,
                    updated_at = :updatedAt,
                    due_date = :dueDate,
                    last_renew_dt = :lastRenewDt,
                    last_transaction_id = :lastTransactionId
                WHERE id = :id and version = :version
                """;
        if (executeUpdate(sql, subscription) == 0) {
            throw new IllegalArgumentException("Subscription with id %s and version %s was not found".formatted(subscription.id().value(), subscription.version()));
        }
    }

    private int executeUpdate(final String sql, final Subscription subscription) {
        final var params = new HashMap<String, Object>();
        params.put("id", subscription.id().value());
        params.put("version", subscription.version());
        params.put("accountId", subscription.accountId().value());
        params.put("planId", subscription.planId().value());
        params.put("status", subscription.status().value());
        params.put("createdAt", subscription.createdAt());
        params.put("updatedAt", subscription.updatedAt());
        params.put("dueDate", subscription.dueDate());
        params.put("lastRenewDt", subscription.lastRenewDate());
        params.put("lastTransactionId", subscription.lastTransactionId());
        return this.database.update(sql, params);
    }

    private RowMap<Subscription> subscriptionMapper() {
        return rs -> {
            return Subscription.with(
                    new SubscriptionId(rs.getString("id")),
                    rs.getInt("version"),
                    new AccountId(rs.getString("account_id")),
                    new PlanId(rs.getLong("plan_id")),
                    JdbcUtil.getLocalDate(rs, "due_date"),
                    rs.getString("status"),
                    JdbcUtil.getInstant(rs, "last_renew_dt"),
                    rs.getString("last_transaction_id"),
                    JdbcUtil.getInstant(rs, "created_at"),
                    JdbcUtil.getInstant(rs, "updated_at")
            );
        };
    }

}
