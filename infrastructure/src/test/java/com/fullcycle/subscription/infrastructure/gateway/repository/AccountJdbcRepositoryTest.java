package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.person.Address;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountJdbcRepositoryTest extends AbstractRepositoryTest {

    @Test
    public void testAssertDependencies() {
        assertNotNull(accountRepository());
    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-johndoe.sql"})
    public void givenPersistedAccount_whenQueriesSuccessfully_shouldReturnIt() {
        // given
        Assertions.assertEquals(1, countAccounts());

        var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedVersion = 0;
        var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        var expectedEmail = new Email("john@gmail.com");
        var expectedName = new Name("John", "Doe");
        var expectedDocument = new Document.Cpf("12312312332");
        var expectedAddress = new Address("12332123", "1", "Casa 1", "BR");

        // when
        var actualAccount = this.accountRepository().accountOfId(expectedId).get();

        // then
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());
    }
}