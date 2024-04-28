package com.fullcycle.subscription.infrastructure.authentication.clientcredentials;

import com.fullcycle.subscription.domain.exceptions.InternalErrorException;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.AuthenticationGateway.AuthenticationResult;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.AuthenticationGateway.ClientCredentialsInput;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.AuthenticationGateway.RefreshTokenInput;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.ClientCredentialsManager.ClientCredentials;
import com.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ClientCredentialsManagerTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private AuthenticationGateway authenticationGateway;

    @InjectMocks
    private ClientCredentialsManager manager;

    @Test
    public void givenValidAuthenticationResult_whenCallsRefresh_shouldCreateCredentials() {
        // given
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        this.manager.refresh();
        final var actualToken = this.manager.retrieve();

        // then
        Assertions.assertEquals(expectedAccessToken, actualToken);
    }

    @Test
    public void givenPreviousAuthentication_whenCallsRefresh_shouldUpdateCredentials() {
        // given
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).refresh(new RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        // when
        this.manager.refresh();

        final var actualCredentials = (ClientCredentials) ReflectionTestUtils.getField(this.manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }

    @Test
    public void givenErrorFromRefreshToken_whenCallsRefresh_shouldFallbackToLogin() {
        // given
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";
        final var expectedClientId = "client-id";
        final var expectedClientSecret = "sad1324213";

        ReflectionTestUtils.setField(this.manager, "credentials", new ClientCredentials(expectedClientId, "acc", "ref"));

        doReturn(expectedClientId).when(keycloakProperties).clientId();
        doReturn(expectedClientSecret).when(keycloakProperties).clientSecret();

        doThrow(InternalErrorException.with("BLA!"))
                .when(authenticationGateway).refresh(new RefreshTokenInput(expectedClientId, expectedClientSecret, "ref"));

        doReturn(new AuthenticationResult(expectedAccessToken, expectedRefreshToken))
                .when(authenticationGateway).login(new ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // when
        this.manager.refresh();

        final var actualCredentials = (ClientCredentials) ReflectionTestUtils.getField(this.manager, "credentials");

        // then
        Assertions.assertEquals(expectedAccessToken, actualCredentials.accessToken());
        Assertions.assertEquals(expectedRefreshToken, actualCredentials.refreshToken());
    }
}