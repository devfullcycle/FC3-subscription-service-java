package com.fullcycle.codeflix.subscription.infrastructure.authentication;

public interface AuthenticationGateway {

    Authentication login(ClientCredentialsInput input);

    Authentication refresh(RefreshTokenInput input);

    record Authentication(String accessToken, String refreshToken) {
    }

    record ClientCredentialsInput(String clientId, String clientSecret) {
    }

    record RefreshTokenInput(String clientId, String clientSecret, String refreshToken) {
    }
}
