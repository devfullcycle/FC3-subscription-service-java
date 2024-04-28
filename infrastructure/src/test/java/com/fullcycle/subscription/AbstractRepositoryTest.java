package com.fullcycle.subscription;

import com.fullcycle.subscription.infrastructure.gateway.repository.AccountJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;

@DataJdbcTest
@Tag("integrationTest")
public class AbstractRepositoryTest extends AbstractTest {

    @Autowired
    private JdbcClient jdbcClient;

    private AccountJdbcRepository accountRepository;

    @BeforeEach
    void setUp() {
        this.accountRepository = new AccountJdbcRepository(jdbcClient);
    }

    protected AccountJdbcRepository accountRepository() {
        return this.accountRepository;
    }
}
