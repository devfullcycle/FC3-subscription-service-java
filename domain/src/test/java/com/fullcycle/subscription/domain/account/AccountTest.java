package com.fullcycle.subscription.domain.account;

import com.fullcycle.subscription.domain.UnitTest;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 1. Caminho feliz de um novo agregado
 * 2. Caminho feliz da restauração do agregado
 * 3. Caminho de validação
 */
public class AccountTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewAccount_ShouldInstantiate() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 0;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");

        // when
        var actualAccount = Account.newAccount(expectedId, expectedUserId, expectedEmail, expectedName, expectedDocument);

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertNull(actualAccount.billingAddress());
    }

    @Test
    public void givenValidParams_whenCallsWith_ShouldInstantiate() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualAccount = Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress);

        // then
        Assertions.assertNotNull(actualAccount);
        Assertions.assertEquals(expectedId, actualAccount.id());
        Assertions.assertEquals(expectedVersion, actualAccount.version());
        Assertions.assertEquals(expectedUserId, actualAccount.userId());
        Assertions.assertEquals(expectedName, actualAccount.name());
        Assertions.assertEquals(expectedEmail, actualAccount.email());
        Assertions.assertEquals(expectedDocument, actualAccount.document());
        Assertions.assertEquals(expectedAddress, actualAccount.billingAddress());
    }

    @Test
    public void givenInvalidId_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'id' should not be null";

        AccountId expectedId = null;
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.message());
    }

    @Test
    public void givenInvalidUserId_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'userId' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        UserId expectedUserId = null;
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.message());
    }

    @Test
    public void givenInvalidName_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'name' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        Name expectedName = null;
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.message());
    }

    @Test
    public void givenInvalidEmail_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'email' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        Email expectedEmail = null;
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.message());
    }

    @Test
    public void givenInvalidDocument_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'document' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        Document expectedDocument = null;
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.message());
    }

    @Test
    public void givenNullAddress_whenCallsWith_ShouldReturnOK() {
        // given
        var expectedMessage = "'id' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        Address expectedAddress = null

        // when
        Assertions.assertDoesNotThrow(
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );
    }
}
