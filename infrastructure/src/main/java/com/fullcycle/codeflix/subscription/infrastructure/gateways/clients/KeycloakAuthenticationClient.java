package com.fullcycle.codeflix.subscription.infrastructure.gateways.clients;

import com.fullcycle.codeflix.subscription.domain.exceptions.InternalErrorException;
import com.fullcycle.codeflix.subscription.infrastructure.authentication.AuthenticationGateway;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.annotations.Keycloak;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class KeycloakAuthenticationClient implements AuthenticationGateway {

    private final RestClient restClient;
    private final String tokenUri;

    public KeycloakAuthenticationClient(
            @Keycloak final RestClient.Builder restClient,
            final KeycloakProperties keycloakProperties
    ) {
        this.restClient = Objects.requireNonNull(restClient).build();
        this.tokenUri =  Objects.requireNonNull(keycloakProperties).tokenUri();
    }

    @Override
    public Authentication login(final ClientCredentialsInput input) {
        final var map = new LinkedMultiValueMap<>();
        map.set("grant_type", "client_credentials");
        map.set("client_id", input.clientId());
        map.set("client_secret", input.clientSecret());

        final var output = this.restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResponse.class);

        if (output == null) {
            throw InternalErrorException.with("Failed to create client credentials [clientId:%s]".formatted(input.clientId()));
        }

        return new Authentication(output.accessToken, output.refreshToken);
    }

    @Override
    public Authentication refresh(final RefreshTokenInput input) {
        final var map = new LinkedMultiValueMap<>();
        map.set("grant_type", "refresh_token");
        map.set("client_id", input.clientId());
        map.set("client_secret", input.clientSecret());
        map.set("refresh_token", input.refreshToken());

        final var output = this.restClient.post()
                .uri(tokenUri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(map)
                .retrieve()
                .body(KeycloakAuthenticationResponse.class);

        if (output == null) {
            throw InternalErrorException.with("Failed to refresh client credentials [clientId:%s]".formatted(input.clientId()));
        }

        return new Authentication(output.accessToken, output.refreshToken);
    }

    public record KeycloakAuthenticationResponse(String accessToken, String refreshToken) {
    }
}
