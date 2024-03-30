package com.fullcycle.codeflix.subscription;

import com.fullcycle.codeflix.subscription.infrastructure.database.DatabaseClient;
import com.fullcycle.codeflix.subscription.infrastructure.database.JdbcClientAdapter;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.AccountRepository;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.EventRepository;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories.PlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@DataJdbcTest
@Tag("integrationTest")
public abstract class AbstractRepositoryTest {

    protected static final String ACCOUNTS = "accounts";
    protected static final String EVENTS = "events";
    protected static final String PLANS = "plans";

    @Autowired
    private JdbcClient jdbcClient;

    private AccountRepository accountRepository;
    private EventRepository eventRepository;
    private PlanRepository planRepository;

    @BeforeEach
    void setUp() {
        this.eventRepository = new EventRepository(dbClient());
        this.accountRepository = new AccountRepository(dbClient(), eventRepository);
        this.planRepository = new PlanRepository(dbClient());
    }

    protected AccountRepository accountRepository() {
        return accountRepository;
    }

    protected EventRepository eventRepository() {
        return eventRepository;
    }

    protected PlanRepository planRepository() {
        return planRepository;
    }

    protected int countAccounts() {
        return countRowsInTable(jdbcClient, ACCOUNTS);
    }

    protected int countEvents() {
        return countRowsInTable(jdbcClient, EVENTS);
    }

    protected int countPlans() {
        return countRowsInTable(jdbcClient, PLANS);
    }

    private int countTable(final String table) {
        return countRowsInTable(jdbcClient, table);
    }

    private DatabaseClient dbClient() {
        return new JdbcClientAdapter(jdbcClient);
    }
}
