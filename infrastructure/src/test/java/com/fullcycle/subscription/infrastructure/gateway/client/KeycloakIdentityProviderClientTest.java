package com.fullcycle.subscription.infrastructure.gateway.client;

import com.fullcycle.subscription.AbstractRestClientTest;
import com.fullcycle.subscription.domain.account.AccountId;
import com.fullcycle.subscription.domain.account.idp.GroupId;
import com.fullcycle.subscription.domain.account.idp.User;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.exceptions.InternalErrorException;
import com.fullcycle.subscription.domain.person.Email;
import com.fullcycle.subscription.domain.person.Name;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.GetClientCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.doReturn;

class KeycloakIdentityProviderClientTest extends AbstractRestClientTest {

    @SpyBean
    private GetClientCredentials getClientCredentials;

    @Autowired
    private KeycloakIdentityProviderClient client;

    @Test
    public void givenValidParams_whenCallsCreateUser_shouldReturnUserId() {
        // given
        var expectedAccountId = new AccountId("ACC-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@doe.com");
        var expectedPassword = "123";
        var expectedUserId = new UserId("b7071c9e-453e-4fd4-9be7-70461f4aa1d7");
        var expectedAuthorization = "token";

        var user = User.newUser(expectedAccountId, expectedName, expectedEmail, expectedPassword);

        stubFor(
                post(urlEqualTo("/admin/realms/test/users"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader("Location", "http://keycloak.internal:8443/admin/realms/test/users/b7071c9e-453e-4fd4-9be7-70461f4aa1d7")
                        )
        );

        doReturn(expectedAuthorization).when(getClientCredentials).retrieve();

        // when
        var actualResponse = this.client.create(user);

        // then
        Assertions.assertEquals(expectedUserId, actualResponse);

        verify(1, postRequestedFor(urlEqualTo("/admin/realms/test/users"))
                .withHeader("Authorization", equalTo("bearer " + expectedAuthorization))
                .withRequestBody(matchingJsonPath("$.firstName", equalTo(expectedName.firstname())))
                .withRequestBody(equalToJson("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "username": "john@doe.com",
                            "email": "john@doe.com",
                            "enabled": true,
                            "attributes": {
                                "account_id": "ACC-123"
                            },
                            "credentials": [
                                {
                                    "type": "password",
                                    "value": "123",
                                    "temporary": false
                                }
                            ]
                        }
                        """))
        );
    }

    @Test
    public void givenUserAlreadyRegistered_whenCallsCreateUser_shouldReturnConflict() {
        // given
        var expectedAccountId = new AccountId("ACC-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@doe.com");
        var expectedPassword = "123";
        var expectedAuthorization = "token";
        var expectedError = "Invalid username or password";

        var user = User.newUser(expectedAccountId, expectedName, expectedEmail, expectedPassword);

        stubFor(
                post(urlEqualTo("/admin/realms/test/users"))
                        .withHeader("Authorization", equalTo("bearer " + expectedAuthorization))
                        .willReturn(aResponse()
                                .withStatus(409)
                                .withBody("""
                                        {"errorMessage":"User exists with same username"}
                                        """)
                        )
        );

        doReturn(expectedAuthorization).when(getClientCredentials).retrieve();

        // when
        var actualError = Assertions.assertThrows(DomainException.class, () -> this.client.create(user));

        // then
        Assertions.assertEquals(expectedError, actualError.getErrors().getFirst().message());
    }

    @Test
    public void givenValidParams_whenCallsAddUserToGroup_shouldBeOk() {
        // given
        var expectedGroupId = new GroupId("GG-123");
        var expectedUserId = new UserId("b7071c9e-453e-4fd4-9be7-70461f4aa1d7");
        var expectedAuthorization = "token";

        stubFor(
                put(urlEqualTo("/admin/realms/test/users/" + expectedUserId.value() + "/groups/" + expectedGroupId.value()))
                        .willReturn(aResponse()
                                .withStatus(204)
                        )
        );

        doReturn(expectedAuthorization).when(getClientCredentials).retrieve();

        // when
        this.client.addUserToGroup(expectedUserId, expectedGroupId);

        // then
        verify(1, putRequestedFor(urlEqualTo("/admin/realms/test/users/b7071c9e-453e-4fd4-9be7-70461f4aa1d7/groups/GG-123"))
                .withHeader("Authorization", equalTo("bearer " + expectedAuthorization))
        );
    }

    @Test
    public void givenEmptyAuthorization_whenCallsAddUserToGroup_shouldReturnError() {
        // given
        var expectedGroupId = new GroupId("GG-123");
        var expectedUserId = new UserId("b7071c9e-453e-4fd4-9be7-70461f4aa1d7");
        var expectedAuthorization = "";
        var expectedMessage = "Error response observed when trying to add user to group";

        stubFor(
                put(urlEqualTo("/admin/realms/test/users/" + expectedUserId.value() + "/groups/" + expectedGroupId.value()))
                        .willReturn(aResponse()
                                .withStatus(401)
                                .withBody("""
                                        {
                                            "error": "HTTP 401 Unauthorized"
                                        }
                                        """)
                        )
        );

        doReturn(expectedAuthorization).when(getClientCredentials).retrieve();

        // when
        var actualError = Assertions.assertThrows(InternalErrorException.class, () -> this.client.addUserToGroup(expectedUserId, expectedGroupId));

        // then
        Assertions.assertEquals(expectedMessage, actualError.getMessage());

        verify(1, putRequestedFor(urlEqualTo("/admin/realms/test/users/b7071c9e-453e-4fd4-9be7-70461f4aa1d7/groups/GG-123"))
                .withHeader("Authorization", equalTo("bearer" + expectedAuthorization))
        );
    }
}