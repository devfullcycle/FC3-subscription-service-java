package com.fullcycle.subscription.infrastructure.gateway.repository;

import com.fullcycle.subscription.AbstractRepositoryTest;
import com.fullcycle.subscription.domain.account.Account;
import com.fullcycle.subscription.domain.account.AccountCreated;
import com.fullcycle.subscription.domain.account.AccountEvent;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.person.Address;
import com.fullcycle.subscription.domain.person.Document;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

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
        var expectedVersion = 1;
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

    @Test
    public void givenEmptyTable_whenPersistSuccessfully_shouldHaveUserAtTheEndOfOperation() {
        // given
        assertEquals(0, countAccounts());

        var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedVersion = 1;
        var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        var expectedEmail = new Email("john@gmail.com");
        var expectedName = new Name("John", "Doe");
        var expectedDocument = new Document.Cpf("12312312332");

        var anAccount = Account.newAccount(expectedId, expectedUserId, expectedEmail, expectedName, expectedDocument);

        // when
        var actualResponse = this.accountRepository().save(anAccount);

        assertEquals(expectedId, actualResponse.id());
        assertEquals(0, actualResponse.version());
        assertEquals(expectedUserId, actualResponse.userId());
        assertEquals(expectedEmail, actualResponse.email());
        assertEquals(expectedName, actualResponse.name());
        assertEquals(expectedDocument, actualResponse.document());
        assertNull(actualResponse.billingAddress());

        // then
        assertEquals(1, countAccounts());

        var actualAccount = this.accountRepository().accountOfId(expectedId).get();

        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedDocument, actualAccount.document());
        assertNull(actualAccount.billingAddress());

        var actualEvents = this.eventRepository().allEventsOfAggregate(expectedId.value(), AccountEvent.TYPE);
        assertEquals(1, actualEvents.size());

        var actualEvent = (AccountCreated) actualEvents.getFirst();
        assertEquals(expectedId.value(), actualEvent.accountId());
        assertEquals(expectedEmail.value(), actualEvent.email());
        assertEquals(expectedName.fullname(), actualEvent.fullname());
        assertNotNull(actualEvent.occurredOn());
    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-johndoe.sql"})
    public void givenJohnDoePersisted_whenUpdateSuccessfully_shouldHaveChangeUserAtTheEndOfOperation() {
        // given
        assertEquals(1, countAccounts());

        var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedVersion = 2;
        var expectedUserId = new UserId("a8b3cf5a-aaaa-4822-9ee8-89e768f6095c");
        var expectedEmail = new Email("johndow@gmail.com");
        var expectedName = new Name("Johhn", "Dow");
        var expectedDocument = new Document.Cpf("33312312332");
        var expectedAddress = new Address("33332123", "3", "Casa 3", "US");

        var anAccount = Account.with(expectedId, 1, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress);

        // when
        var actualResponse = this.accountRepository().save(anAccount);

        assertEquals(expectedId, actualResponse.id());
        assertEquals(1, actualResponse.version());
        assertEquals(expectedUserId, actualResponse.userId());
        assertEquals(expectedEmail, actualResponse.email());
        assertEquals(expectedName, actualResponse.name());
        assertEquals(expectedDocument, actualResponse.document());
        assertEquals(expectedAddress, actualResponse.billingAddress());

        // then
        assertEquals(1, countAccounts());

        var actualAccount = this.accountRepository().accountOfId(expectedId).get();

        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());
    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-account-johndoe.sql"})
    public void givenJohnDoePersisted_whenQueryByUserIdSuccessfully_shouldReturnAccount() {
        // given
        assertEquals(1, countAccounts());

        var expectedId = new AccountId("033c7d9eb3cc4eb7840b942fa2194cab");
        var expectedVersion = 1;
        var expectedUserId = new UserId("a8b3cf5a-5f81-4822-9ee8-89e768f6095c");
        var expectedEmail = new Email("john@gmail.com");
        var expectedName = new Name("John", "Doe");
        var expectedDocument = new Document.Cpf("12312312332");
        var expectedAddress = new Address("12332123", "1", "Casa 1", "BR");

        // when
        var actualResponse = this.accountRepository().accountOfUserId(expectedUserId).get();

        // then
        assertEquals(expectedId, actualResponse.id());
        assertEquals(expectedVersion, actualResponse.version());
        assertEquals(expectedUserId, actualResponse.userId());
        assertEquals(expectedEmail, actualResponse.email());
        assertEquals(expectedName, actualResponse.name());
        assertEquals(expectedDocument, actualResponse.document());
        assertEquals(expectedAddress, actualResponse.billingAddress());
    }
}