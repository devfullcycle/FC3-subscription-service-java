package com.fullcycle.subscription;

import com.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
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

    @BeforeEach
    void setUp() {
        this.accountRepository = new AccountJdbcRepository(jdbcClient);
    }

    protected int countAccounts() {
        return JdbcTestUtils.countRowsInTable(jdbcClient, ACCOUNTS_TABLE);
    }

    protected AccountJdbcRepository accountRepository() {
        return this.accountRepository;
    }
}
