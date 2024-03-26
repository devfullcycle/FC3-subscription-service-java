package com.fullcycle.codeflix.subscription.infrastructure.authentication;

import com.fullcycle.codeflix.subscription.AbstractRestClientTest;
import com.fullcycle.codeflix.subscription.infrastructure.authentication.AuthenticationGateway.ClientCredentialsInput;
import com.fullcycle.codeflix.subscription.infrastructure.authentication.AuthenticationGateway.RefreshTokenInput;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.KeycloakAuthenticationClient;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.KeycloakAuthenticationClient.KeycloakAuthenticationResponse;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KeycloakAuthenticationClientTest extends AbstractRestClientTest {

    @Autowired
    private KeycloakAuthenticationClient gateway;

    @Test
    public void givenValidParams_whenCallsLogin_shouldReturnClientCredentials() {
        // given
        final var expectedClientId = "client-123";
        final var expectedClientSecret = "asdej1o123";
        final var expectedAccessToken = "access";
        final var expectedRefreshToken = "refresh";

        stubFor(
                post(urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                        .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(Json.writeValueAsString(new KeycloakAuthenticationResponse(expectedAccessToken, expectedRefreshToken)))
                        )
        );

        // when
        final var actualOutput =
                this.gateway.login(new ClientCredentialsInput(expectedClientId, expectedClientSecret));

        // then
        assertEquals(expectedAccessToken, actualOutput.accessToken());
        assertEquals(expectedRefreshToken, actualOutput.refreshToken());
    }

    @Test
    public void givenValidParams_whenCallsRefresh_shouldReturnClientCredentials() {
        // given
        final var expectedClientId = "client-123";
        final var expectedClientSecret = "asdej1o123";
        final var expectedAccessToken = "access2";
        final var expectedRefreshToken = "refresh2";

        stubFor(
                post(urlPathEqualTo("/realms/test/protocol/openid-connect/token"))
                        .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_JSON_VALUE))
                        .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(Json.writeValueAsString(new KeycloakAuthenticationResponse(expectedAccessToken, expectedRefreshToken)))
                        )
        );

        // when
        final var actualOutput =
                this.gateway.refresh(new RefreshTokenInput(expectedClientId, expectedClientSecret, "refresh"));

        // then
        assertEquals(expectedAccessToken, actualOutput.accessToken());
        assertEquals(expectedRefreshToken, actualOutput.refreshToken());
    }
}