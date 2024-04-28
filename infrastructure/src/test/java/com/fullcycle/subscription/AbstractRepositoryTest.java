package com.fullcycle.subscription;

import com.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import com.fullcycle.subscription.infrastructure.gateway.repository.EventJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.jdbc.JdbcTestUtils;

@DataJdbcTest
@Tag("integrationTest")
public class AbstractRepositoryTest extends AbstractTest {

    private static final String ACCOUNTS_TABLE = "accounts";

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;
    private EventJdbcRepository eventRepository;
    @BeforeEach
    void setUp() {
        this.eventRepository = new EventJdbcRepository(jdbcClient);
        this.accountRepository = new AccountJdbcRepository(jdbcClient, eventRepository);
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNTS_TABLE);
    }

    protected AccountJdbcRepository accountRepository() {
        return this.accountRepository;
    }

    protected EventJdbcRepository eventRepository() {
        return eventRepository;
    }
}
