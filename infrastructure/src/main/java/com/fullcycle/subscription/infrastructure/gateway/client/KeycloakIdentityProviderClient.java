package com.fullcycle.subscription.infrastructure.gateway.client;

import com.fullcycle.subscription.domain.account.idp.GroupId;
import com.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import com.fullcycle.subscription.domain.account.idp.User;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.exceptions.DomainException;
import com.fullcycle.subscription.domain.exceptions.InternalErrorException;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.GetClientCredentials;
import com.fullcycle.subscription.infrastructure.configuration.annotations.KeycloakAdmin;
import com.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.*;

@Component
public class KeycloakIdentityProviderClient implements IdentityProviderGateway {

    private static final Logger log = LoggerFactory.getLogger(KeycloakIdentityProviderClient.class);

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final GetClientCredentials getClientCredentials;

    public KeycloakIdentityProviderClient(
            @KeycloakAdmin final RestClient restClient,
            final KeycloakProperties keycloakProperties,
            final GetClientCredentials getClientCredentials
    ) {
        this.restClient = Objects.requireNonNull(restClient);
        this.keycloakProperties = Objects.requireNonNull(keycloakProperties);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public UserId create(final User anUser) {
        final var account = anUser.accountId().value();
        log.info("Creating user with Keycloak Provider [accountId:{}]", account);

        final var map = new HashMap<String, Object>();
        map.put("firstName", anUser.name().firstname());
        map.put("lastName", anUser.name().lastname());
        map.put("username", anUser.email().value());
        map.put("email", anUser.email().value());
        map.put("enabled", Boolean.TRUE);
        map.put("attributes", Map.of(
                "account_id", account
        ));
        map.put("credentials", List.of(Map.of(
                "type", "password",
                "value", anUser.password(),
                "temporary", Boolean.FALSE
        )));

        try {
            final var res = this.restClient.post()
                    .uri(this.keycloakProperties.adminUsersUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "bearer " + getClientCredentials.retrieve())
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();

            if (res.getStatusCode() != HttpStatus.CREATED) {
                throw InternalErrorException.with("Unexpected Keycloak response [status:%s]".formatted(res.getStatusCode().value()));
            }

            final var actualUserId = getUserId(res.getHeaders());
            log.info("User created successfully on Keycloak [accountId:{}] [userId:{}]", account, actualUserId);
            return new UserId(actualUserId);
        } catch (HttpClientErrorException.Conflict ex) {
            log.info("Conflict response observed from Keycloak [accountId:{}] [response:{}]", account, ex.getResponseBodyAsString());
            throw DomainException.with("Invalid username or password");
        } catch (HttpStatusCodeException ex) {
            log.info("Error response observed from Keycloak [accountId:{}] [response:{}]", account, ex.getResponseBodyAsString());
            throw InternalErrorException.with(ex.getMessage());
        }
    }

    @Override
    public void addUserToGroup(final UserId anId, final GroupId aGroupId) {

    }

    @Override
    public void removeUserFromGroup(final UserId anId, final GroupId aGroupId) {

    }

    private String getUserId(final HttpHeaders headers) {
        return Optional.ofNullable(headers)
                .map(HttpHeaders::getLocation)
                .map(URI::getPath)
                .map(it -> List.of(it.split("/")).getLast())
                .orElse(null);
    }
}
