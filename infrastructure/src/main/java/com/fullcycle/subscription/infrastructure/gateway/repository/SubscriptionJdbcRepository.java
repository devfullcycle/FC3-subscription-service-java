package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.plan.PlanId;
import com.fullcycle.subscription.domain.subscription.Subscription;
import com.fullcycle.subscription.domain.subscription.SubscriptionGateway;
import com.fullcycle.subscription.domain.subscription.SubscriptionId;
import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.RowMap;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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
    public Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId) {
        return Optional.empty();
    }

    @Override
    public Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId) {
        final var sql = "SELECT id, version, account_id, plan_id, status, created_at, updated_at, due_date, last_renew_dt, last_transaction_id FROM subscriptions WHERE id = :id";
        return this.database.queryOne(sql, Map.of("id", subscriptionId.value()), subscriptionMapper());
    }

    @Override
    public Subscription save(Subscription subscription) {
        return null;
    }

    @Override
    public SubscriptionId nextId() {
        return null;
    }

    private RowMap<Subscription> subscriptionMapper() {
        return rs -> {
            return Subscription.with(
                    new SubscriptionId(rs.getString("id")),
                    rs.getInt("version"),
                    new AccountId(rs.getString("account_id")),
                    new PlanId(rs.getLong("plan_id")),
                    rs.getDate("due_date").toLocalDate(),
                    rs.getString("status"),
                    rs.getObject("last_renew_dt", Instant.class),
                    rs.getString("last_transaction_id"),
                    rs.getObject("created_at", Instant.class),
                    rs.getObject("updated_at", Instant.class)
            );
        };
    }

}
