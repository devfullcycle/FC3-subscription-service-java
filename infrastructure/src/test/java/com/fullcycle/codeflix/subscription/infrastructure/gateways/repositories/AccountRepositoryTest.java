package com.fullcycle.codeflix.subscription.infrastructure.gateways.repositories;

import com.fullcycle.codeflix.subscription.AbstractRepositoryTest;
import com.fullcycle.codeflix.subscription.domain.account.*;
import com.fullcycle.codeflix.subscription.domain.account.iam.UserId;
import com.fullcycle.codeflix.subscription.domain.person.Address;
import com.fullcycle.codeflix.subscription.domain.person.Document;
import com.fullcycle.codeflix.subscription.domain.person.Email;
import com.fullcycle.codeflix.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

public class AccountRepositoryTest extends AbstractRepositoryTest {

    @Test
    @Sql({"classpath:/sql/accounts/seed-accounts.sql"})
    public void testListAll() {
        final var expectedUsers = 2;
        final var actualUsers = this.accountRepository().allAccounts();
        Assertions.assertEquals(expectedUsers, actualUsers.size());
    }

    @Test
    public void testCreateNewUser() {
        // given
        Assertions.assertEquals(0, countAccounts());

        final var expectedId = new AccountId("123");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("456");
        final var expectedEmail = new Email("john@email.com");
        final var expectedName = new Name("John", "Doe");
        final var expectedDocument = Document.create("12332112322", Document.Cpf.TYPE);

        final var john = Account.newAccount(expectedId, expectedUserId, expectedEmail, expectedName, expectedDocument);

        // when
        this.accountRepository().save(john);

        // then
        Assertions.assertEquals(1, countAccounts());

        final var actualUser = this.accountRepository().accountOfId(expectedId).get();
        Assertions.assertEquals(expectedId, actualUser.id());
        Assertions.assertEquals(expectedVersion, actualUser.version());
        Assertions.assertEquals(expectedName, actualUser.name());
        Assertions.assertNull(actualUser.billingAddress());
        Assertions.assertEquals(expectedDocument, actualUser.document());

        final var actualEvents = this.eventRepository().allEventsOfAggregate(AccountEvent.AGGREGATE_TYPE, expectedId.value());
        Assertions.assertEquals(1, actualEvents.size());

        final var actualAccountEvent = (AccountCreated) actualEvents.getFirst();
        Assertions.assertEquals(expectedId.value(), actualAccountEvent.accountId());
        Assertions.assertEquals(expectedEmail.value(), actualAccountEvent.email());
        Assertions.assertEquals(expectedName.fullname(), actualAccountEvent.fullname());
        Assertions.assertNotNull(actualAccountEvent.occurredOn());
    }

    @Test
    public void testCreateUserWithAddress() {
        // given
        Assertions.assertTrue(this.accountRepository().allAccounts().isEmpty());

        final var expectedId = new AccountId("123");
        final var expectedVersion = 1;
        final var expectedUserId = new UserId("456");
        final var expectedEmail = new Email("john@email.com");
        final var expectedName = new Name("John", "Doe");
        final var expectedDocument = Document.create("12332112322", Document.Cpf.TYPE);
        final var expectedAddress = new Address("01123909", "1", "03", "US");

        final var john = Account.newAccount(expectedId, expectedUserId, expectedEmail, expectedName, expectedDocument);
        john.execute(new AccountCommand.ChangeBillingAddressCommand(expectedAddress));

        // when
        this.accountRepository().save(john);

        // then
        Assertions.assertEquals(1, this.accountRepository().allAccounts().size());

        final var actualUser = this.accountRepository().accountOfId(expectedId).get();
        Assertions.assertEquals(expectedId, actualUser.id());
        Assertions.assertEquals(expectedVersion, actualUser.version());
        Assertions.assertEquals(expectedName, actualUser.name());
        Assertions.assertEquals(expectedAddress, actualUser.billingAddress());
        Assertions.assertEquals(expectedDocument, actualUser.document());

        final var actualEvents = this.eventRepository().allEventsOfAggregate(AccountEvent.AGGREGATE_TYPE, expectedId.value());
        Assertions.assertEquals(1, actualEvents.size());

        final var actualAccountEvent = (AccountCreated) actualEvents.getFirst();
        Assertions.assertEquals(expectedId.value(), actualAccountEvent.accountId());
        Assertions.assertEquals(expectedEmail.value(), actualAccountEvent.email());
        Assertions.assertEquals(expectedName.fullname(), actualAccountEvent.fullname());
        Assertions.assertNotNull(actualAccountEvent.occurredOn());
    }

    @Test
    @Sql({"classpath:/sql/accounts/seed-johndoe.sql"})
    public void testUpdateUserWithAddress() {
        // given
        Assertions.assertEquals(1, this.accountRepository().allAccounts().size());

        final var expectedId = new AccountId("456");
        final var expectedVersion = 11;
        final var expectedName = new Name("Lucian", "Does");
        final var expectedDocument = Document.create("36768647000105", Document.Cnpj.TYPE);
        final var expectedAddress = new Address("01123909", "33", "03", "BR");

        final var john = this.accountRepository().accountOfId(expectedId).get();
        john.execute(
                new AccountCommand.ChangeNameCommand(expectedName),
                new AccountCommand.ChangeBillingAddressCommand(expectedAddress),
                new AccountCommand.ChangeDocumentCommand(expectedDocument)
        );

        // when
        this.accountRepository().save(john);

        // then
        Assertions.assertEquals(1, this.accountRepository().allAccounts().size());

        final var actualUser = this.accountRepository().accountOfId(expectedId).get();
        Assertions.assertEquals(expectedId, actualUser.id());
        Assertions.assertEquals(expectedVersion, actualUser.version());
        Assertions.assertEquals(expectedName, actualUser.name());
        Assertions.assertEquals(expectedAddress, actualUser.billingAddress());
        Assertions.assertEquals(expectedDocument, actualUser.document());
    }
}
