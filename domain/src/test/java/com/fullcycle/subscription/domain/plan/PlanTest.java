package com.fullcycle.subscription.domain.plan;

import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.money.Money;
import com.fullcycle.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

/**
 * 1. Caminho feliz de um novo agregado
 * 2. Caminho feliz da restauração do agregado
 * 3. Caminho de validação
 */
public class PlanTest {

    @Test
    public void givenValidParams_whenCallsNewPlan_ShouldInstantiate() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = true;
        var expectedPrice = new Money("BRL", 20.99);

        // when
        var actualPlan = Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenNullActive_whenCallsNewPlan_ShouldInstantiate() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);

        // when
        var actualPlan = Plan.newPlan(expectedId, expectedName, expectedDescription, null, expectedPrice);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNotNull(actualPlan.deletedAt());
    }

    @Test
    public void givenValidParams_whenCallsWith_ShouldInstantiate() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();

        // when
        var actualPlan = Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'id' should not be null";

        PlanId expectedId = null;
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidName_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'name' should not be empty";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullName_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'name' should not be empty";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        String expectedName = null;
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidDescription_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'description' should not be empty";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = "";
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullDescription_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'description' should not be empty";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        String expectedDescription = null;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullActive_whenCallsWith_ShouldReturnFalse() {
        // given
        var expectedMessage = "'active' should not be null";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        Boolean expectedActive = null;
        var expectedPrice = new Money("BRL", 20.99);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullPrice_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'price' should not be null";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = "description";
        var expectedActive = false;
        Money expectedPrice = null;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void given0AsPrice_whenCallsWith_ShouldReturnOK() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 0.0);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();


        // when
        var actualPlan = Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenNullCreatedAt_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'createdAt' should not be null";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 0.0);
        Instant expectedCreatedAt = null;
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDeletedAt = InstantUtils.now();

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullUpdatedAt_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'updatedAt' should not be null";

        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 0.0);
        var expectedCreatedAt = InstantUtils.now();
        Instant expectedUpdatedAt = null;
        var expectedDeletedAt = InstantUtils.now();

        // when
        var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullDeletedAt_whenCallsWith_ShouldReturnOK() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 0;
        var expectedName = "Plus";
        var expectedDescription = """
                lero lero
                """;
        var expectedActive = true;
        var expectedPrice = new Money("BRL", 0.0);
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        Instant expectedDeletedAt = null;

        // when
        var actualPlan = Plan.with(expectedId, expectedVersion, expectedName, expectedDescription, expectedActive, expectedPrice, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedId, actualPlan.id());
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertEquals(expectedCreatedAt, actualPlan.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualPlan.updatedAt());
        Assertions.assertEquals(expectedDeletedAt, actualPlan.deletedAt());
    }

    @Test
    public void givenPlan_whenExecuteWithoutCommands_ShouldDoNothing() {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 1;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = true;
        var expectedPrice = new Money("BRL", 20.99);

        var actualPlan = Plan.newPlan(expectedId, expectedName, expectedDescription, expectedActive, expectedPrice);

        // when
        actualPlan.execute();

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertNotNull(actualPlan.updatedAt());
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenInactivePlan_whenExecuteActiveCommand_ShouldActivate() throws InterruptedException {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 1;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = true;
        var expectedPrice = new Money("BRL", 20.99);

        var actualPlan = Plan.newPlan(expectedId, expectedName, expectedDescription, false, expectedPrice);
        Assertions.assertFalse(actualPlan.active());
        Assertions.assertNotNull(actualPlan.deletedAt());
        Thread.sleep(1); // Sleep for updatedAt time change

        // when
        actualPlan.execute(new PlanCommand.ActivatePlan());

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNull(actualPlan.deletedAt());
    }

    @Test
    public void givenActivePlan_whenExecuteInactivateCommand_ShouldInactivate() throws InterruptedException {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 1;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = false;
        var expectedPrice = new Money("BRL", 20.99);

        var actualPlan = Plan.newPlan(expectedId, expectedName, expectedDescription, true, expectedPrice);
        Assertions.assertTrue(actualPlan.active());
        Assertions.assertNull(actualPlan.deletedAt());
        Thread.sleep(1); // Sleep for updatedAt time change

        // when
        actualPlan.execute(new PlanCommand.InactivatePlan());

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNotNull(actualPlan.deletedAt());
    }

    @Test
    public void givenPlan_whenExecuteChangeCommand_ShouldUpdateAttributes() throws InterruptedException {
        // given
        var expectedId = new PlanId(123L);
        var expectedVersion = 1;
        var expectedName = "Plus";
        var expectedDescription = """
                Lero lero
                """;
        var expectedActive = true;
        var expectedPrice = new Money("BRL", 20.99);

        var actualPlan = Plan.newPlan(expectedId, "Freemium", "Lá", false, new Money("USD", 2.99));
        Thread.sleep(1); // Sleep for updatedAt time change

        // when
        actualPlan.execute(new PlanCommand.ChangePlan(expectedName, expectedDescription, expectedPrice, expectedActive));

        // then
        Assertions.assertNotNull(actualPlan);
        Assertions.assertEquals(expectedVersion, actualPlan.version());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(expectedActive, actualPlan.active());
        Assertions.assertEquals(expectedPrice, actualPlan.price());
        Assertions.assertNotNull(actualPlan.createdAt());
        Assertions.assertTrue(actualPlan.createdAt().isBefore(actualPlan.updatedAt()));
        Assertions.assertNull(actualPlan.deletedAt());
    }
}
