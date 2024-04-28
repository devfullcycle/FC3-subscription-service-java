package com.fullcycle.subscription;

import com.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.PlanJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.SubscriptionJdbcRepository;
import com.fullcycle.subscription.infrastructure.jdbc.JdbcClientAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;

@DataJdbcTest
@Tag("integrationTest")
public class AbstractRepositoryTest extends AbstractTest {

    private static final String ACCOUNT_TABLE = "accounts";
    private static final String PLAN_TABLE = "plans";
    private static final String SUBSCRIPTION_TABLE = "subscriptions";

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;
    private PlanJdbcRepository planRepository;
    private EventJdbcRepository eventRepository;
    private SubscriptionJdbcRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        this.eventRepository = new EventJdbcRepository(new JdbcClientAdapter(jdbcClient));
        this.accountRepository = new AccountJdbcRepository(new JdbcClientAdapter(jdbcClient), eventRepository);
        this.planRepository = new PlanJdbcRepository(new JdbcClientAdapter(jdbcClient));
        this.subscriptionRepository = new SubscriptionJdbcRepository(new JdbcClientAdapter(jdbcClient), eventRepository);
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNT_TABLE);
    }

    protected int countPlans() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, PLAN_TABLE);
    }

    protected int countSubscriptions() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, SUBSCRIPTION_TABLE);
    }

    protected AccountJdbcRepository accountRepository() {
        return this.accountRepository;
    }

    protected EventJdbcRepository eventRepository() {
        return eventRepository;
    }

    protected PlanJdbcRepository planRepository() {
        return planRepository;
    }

    public SubscriptionJdbcRepository subscriptionRepository() {
        return subscriptionRepository;
    }
}
