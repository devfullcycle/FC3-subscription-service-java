package com.fullcycle.subscription;

import com.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.PlanJdbcRepository;
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

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;
    private PlanJdbcRepository planRepository;
    private EventJdbcRepository eventRepository;

    @BeforeEach
    void setUp() {
        this.eventRepository = new EventJdbcRepository(new JdbcClientAdapter(jdbcClient));
        this.accountRepository = new AccountJdbcRepository(new JdbcClientAdapter(jdbcClient), eventRepository);
        this.planRepository = new PlanJdbcRepository(new JdbcClientAdapter(jdbcClient));
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNT_TABLE);
    }

    protected int countPlans() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, PLAN_TABLE);
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
}
