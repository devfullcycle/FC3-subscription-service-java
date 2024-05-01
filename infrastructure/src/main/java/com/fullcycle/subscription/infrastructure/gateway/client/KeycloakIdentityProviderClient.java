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
import com.fullcycle.subscription.infrastructure.utils.KeycloakUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class KeycloakIdentityProviderClient implements IdentityProviderGateway {

    private static final Logger log = LoggerFactory.getLogger(KeycloakIdentityProviderClient.class);
    public static final String USER_GROUP_PATH = "/{id}/groups/{groupId}";

    private final RestClient restClient;
    private final GetClientCredentials getClientCredentials;
    private final String adminUsersUri;

    public KeycloakIdentityProviderClient(
            @KeycloakAdmin final RestClient restClient,
            final GetClientCredentials getClientCredentials,
            final KeycloakProperties keycloakProperties
    ) {
        this.restClient = Objects.requireNonNull(restClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
        this.adminUsersUri = Objects.requireNonNull(keycloakProperties.adminUsersUri());
    }

    /**
     * The client credentials used to perform this operation needs to have the service role: "realm-management - manage-users"
     *
     * @param anUser
     * @return
     */
    @Override
    public UserId create(final User anUser) {
        log.info("Creating user with Keycloak IDP [accountId:{}]", anUser.accountId().value());

        final var map = new HashMap<>();
        map.put("firstName", anUser.name().firstname());
        map.put("lastName", anUser.name().lastname());
        map.put("username", anUser.email().value());
        map.put("email", anUser.email().value());
        map.put("enabled", true);
        map.put("attributes", Map.of(
                "account_id", anUser.accountId().value()
        ));
        map.put("credentials", List.of(Map.of(
                "type", "password",
                "value", anUser.password(),
                "temporary", false
        )));

        try {
            final var res = this.restClient.post()
                    .uri(this.adminUsersUri)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "bearer " + getClientCredentials.retrieve())
                    .body(map)
                    .retrieve()
                    .toBodilessEntity();

            if (res.getStatusCode() != HttpStatus.CREATED) {
                throw InternalErrorException.with("Unexpected status code from Keycloak [status:%s]".formatted(res.getStatusCode()));
            }
            final var userId = mapUserId(res);
            log.debug("User created successfully on Keycloak [accountId:{}] [userId:{}]", anUser.accountId().value(), userId.value());
            return userId;
        } catch (final HttpClientErrorException.Conflict ex) {
            log.error("Conflict observed from Keycloak after create user attempt [accountId:{}]: {}", anUser.accountId().value(), ex.getMessage());
            throw DomainException.with("Invalid username or password");
        } catch (final Throwable t) {
            log.error("Unexpected error observed from Keycloak after create user attempt [accountId:{}]: {}", anUser.accountId().value(), t.getMessage());
            throw InternalErrorException.with(t.getMessage());
        }
    }

    @Override
    public void addUserToGroup(UserId userId, GroupId aGroupId) {
        log.info("Adding user to group on Keycloak IDP [userId:{}] [groupId:{}]", userId.value(), aGroupId.value());
        try {
            final var res = this.restClient.put()
                    .uri(this.adminUsersUri + USER_GROUP_PATH, userId.value(), aGroupId.value())
                    .header(HttpHeaders.AUTHORIZATION, "bearer " + getClientCredentials.retrieve())
                    .retrieve()
                    .toBodilessEntity();

            if (!res.getStatusCode().is2xxSuccessful()) {
                throw InternalErrorException.with("Unexpected status code from Keycloak [status:%s]".formatted(res.getStatusCode()));
            }
            log.debug("User added successfully on Keycloak [userId:{}] [groupId:{}]", userId.value(), aGroupId.value());
        } catch (final Throwable t) {
            log.error("Unexpected error observed from Keycloak after add user to group attempt [userId:{}] [groupId:{}]: {}", userId.value(), aGroupId.value(), t.getMessage());
            throw InternalErrorException.with(t.getMessage());
        }
    }

    @Override
    public void removeUserFromGroup(UserId userId, GroupId aGroupId) {
        log.info("Removing user to group on Keycloak IDP [userId:{}] [groupId:{}]", userId.value(), aGroupId.value());
        try {
            final var res = this.restClient.delete()
                    .uri(this.adminUsersUri + USER_GROUP_PATH, userId.value(), aGroupId.value())
                    .header(HttpHeaders.AUTHORIZATION, "bearer " + getClientCredentials.retrieve())
                    .retrieve()
                    .toBodilessEntity();

            if (!res.getStatusCode().is2xxSuccessful()) {
                throw InternalErrorException.with("Unexpected status code from Keycloak [status:%s]".formatted(res.getStatusCode()));
            }
            log.debug("User removed successfully on Keycloak [userId:{}] [groupId:{}]", userId.value(), aGroupId.value());
        } catch (final Throwable t) {
            log.error("Unexpected error observed from Keycloak after remove user to group attempt [userId:{}] [groupId:{}]: {}", userId.value(), aGroupId.value(), t.getMessage());
            throw InternalErrorException.with(t.getMessage());
        }
    }

    private UserId mapUserId(ResponseEntity<Void> res) {
        final var id = KeycloakUtils.extractIdFromLocation(res.getHeaders().getLocation());
        if (id == null) {
            throw InternalErrorException.with("Failed to create user");
        }
        return new UserId(id);
    }
}
