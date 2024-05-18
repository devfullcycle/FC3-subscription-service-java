package com.fullcycle.subscription.infrastructure.gateway.client;

import com.fullcycle.subscription.domain.account.idp.GroupId;
import com.fullcycle.subscription.domain.account.idp.IdentityProviderGateway;
import com.fullcycle.subscription.domain.account.idp.User;
import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.domain.utils.IdUtils;
import com.fullcycle.subscription.infrastructure.authentication.clientcredentials.GetClientCredentials;
import com.fullcycle.subscription.infrastructure.configuration.properties.KeycloakProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class KeycloakIdentityProviderClient implements IdentityProviderGateway {

    private final RestClient restClient;
    private final KeycloakProperties keycloakProperties;
    private final GetClientCredentials getClientCredentials;

    public KeycloakIdentityProviderClient(
            final RestClient restClient,
            final KeycloakProperties keycloakProperties,
            final GetClientCredentials getClientCredentials
    ) {
        this.restClient = Objects.requireNonNull(restClient);
        this.keycloakProperties = Objects.requireNonNull(keycloakProperties);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public UserId create(User anUser) {
        return new UserId(IdUtils.uniqueId());
    }

    @Override
    public void addUserToGroup(UserId anId, GroupId aGroupId) {

    }

    @Override
    public void removeUserFromGroup(UserId anId, GroupId aGroupId) {

    }
}
