package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.domain.account.AccountId;
import com.fullcycle.codeflix.subscription.domain.plan.PlanId;
import com.fullcycle.codeflix.subscription.domain.subscription.Subscription;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.codeflix.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.codeflix.subscription.domain.utils.IdUtils;
import com.fullcycle.codeflix.subscription.infrastructure.database.DatabaseClient;
import com.fullcycle.codeflix.subscription.infrastructure.database.RowMapping;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class SubscriptionRepository implements SubscriptionGateway {

    private static final RowMapping<Subscription> rowMapping = rowMapping();

    private final DatabaseClient database;
    private final EventRepository eventRepository;

    public SubscriptionRepository(
            final DatabaseClient databaseClient,
            final EventRepository eventRepository
    ) {
        this.database = Objects.requireNonNull(databaseClient);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    @Override
    public SubscriptionId nextId() {
        return new SubscriptionId(IdUtils.uniqueId());
    }

    @Override
    public Optional<Subscription> subscriptionOfId(final SubscriptionId subscriptionId) {
        final var sql = "SELECT id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id FROM subscriptions WHERE id = :id";
        final var rows = this.database.query(sql, Map.of("id", subscriptionId.value()), rowMapping);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    @Override
    public Optional<Subscription> latestSubscriptionOfUser(final AccountId accountId) {
        final var sql = "SELECT id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id FROM subscriptions WHERE account_id = :accountId ORDER BY created_at DESC";
        final var rows = this.database.query(sql, Map.of("accountId", accountId.value()), rowMapping);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rows.getFirst());
    }

    @Override
    public void save(final Subscription subscription) {
        if (subscription.version() == 0) {
            create(subscription);
        } else {
            update(subscription);
        }

        this.eventRepository.saveAll(subscription.domainEvents());
    }

    private void create(final Subscription sub) {
        final var sql = """
                INSERT INTO subscriptions (id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id)
                VALUES (:id, (:version + 1), :accountId, :planId, :status, :createdAt, :updatedAt, :dueDate, :lastRenewDt, :lastTransactionId)
                """;
        executeUpdate(sql, sub);
    }

    private void update(final Subscription sub) {
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
                WHERE id = :id AND version = :version
                """;

        if (executeUpdate(sql, sub) == 0) {
            throw new IllegalStateException("0 rows affected");
        }
    }

    private int executeUpdate(final String sql, final Subscription sub) {
        final var params = new HashMap<String, Object>();
        params.put("id", sub.id().value());
        params.put("version", sub.version());
        params.put("accountId", sub.accountId().value());
        params.put("planId", sub.planId().value());
        params.put("status", sub.status().value());
        params.put("createdAt", sub.createdAt());
        params.put("updatedAt", sub.updatedAt());
        params.put("dueDate", sub.dueDate());
        params.put("lastRenewDt", sub.lastRenewDate());
        params.put("lastTransactionId", sub.lastTransactionId());

        try {
            return this.database.update(sql, params);
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }

    private static RowMapping<Subscription> rowMapping() {
        return rs -> Subscription.with(
                new SubscriptionId(rs.getString("id")),
                rs.getInt("version"),
                new AccountId(rs.getString("account_id")),
                new PlanId(rs.getString("plan_id")),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant(),
                rs.getString("status"),
                rs.getDate("due_date").toLocalDate(),
                rs.getTimestamp("last_renew_dt").toInstant(),
                rs.getString("last_transaction_id")
        );
    }
}