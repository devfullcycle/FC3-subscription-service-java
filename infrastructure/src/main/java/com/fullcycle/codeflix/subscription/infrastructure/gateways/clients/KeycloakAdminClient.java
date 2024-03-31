package com.fullcycle.codeflix.subscription.infrastructure.gateways.clients;

import com.fullcycle.codeflix.subscription.domain.account.iam.GroupId;
import com.fullcycle.codeflix.subscription.domain.account.iam.IdentityGateway;
import com.fullcycle.codeflix.subscription.domain.account.iam.User;
import com.fullcycle.codeflix.subscription.domain.account.iam.UserId;
import com.fullcycle.codeflix.subscription.infrastructure.authentication.GetClientCredentials;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.annotations.Keycloak;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.properties.KeycloakProperties;
import com.fullcycle.codeflix.subscription.infrastructure.gateways.clients.models.UserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class KeycloakAdminClient implements IdentityGateway, HttpClient {

    public static final String NAMESPACE = "keycloak";

    private final GetClientCredentials getClientCredentials;
    private final RestClient restClient;

    public KeycloakAdminClient(
            @Keycloak final RestClient.Builder restClient,
            final GetClientCredentials getClientCredentials,
            final KeycloakProperties properties
    ) {
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
        this.restClient = Objects.requireNonNull(restClient)
                .baseUrl(properties.adminUri())
                .build();
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Override
    public UserId create(final User anUser) {
        final var email = anUser.email();
        final var token = this.getClientCredentials.retrieve();
        final var res = doMutate(email, () ->
                this.restClient.post()
                        .uri("/users")
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(email))
                        .onStatus(is5xx, a5xxHandler(email))
                        .body(UserResponse.class)
        );
        return new UserId(res.id());
    }

    @Override
    public void addUserToGroup(final UserId anUser, final GroupId groupId) {
        final var userId = anUser.value();
        final var token = this.getClientCredentials.retrieve();
        doMutate(userId, () ->
                this.restClient.put()
                        .uri("/users/{id}/groups/{groupId}", userId, groupId.value())
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(userId))
                        .onStatus(is5xx, a5xxHandler(userId))
                        .toBodilessEntity()
        );
    }

    @Override
    public void removeUserFromGroup(final UserId anUser, final GroupId groupId) {
        final var userId = anUser.value();
        final var token = this.getClientCredentials.retrieve();
        doMutate(userId, () ->
                this.restClient.delete()
                        .uri("/users/{id}/groups/{groupId}", userId, groupId.value())
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(userId))
                        .onStatus(is5xx, a5xxHandler(userId))
                        .toBodilessEntity()
        );
    }
}
