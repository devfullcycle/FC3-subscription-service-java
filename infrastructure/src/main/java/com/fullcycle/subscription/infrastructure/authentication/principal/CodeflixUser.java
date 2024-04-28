package com.fullcycle.subscription.infrastructure.authentication.principal;

import com.fullcycle.subscription.domain.account.idp.UserId;
import com.fullcycle.subscription.infrastructure.exceptions.ForbiddenException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public record CodeflixUser(
        String name,
        String idpUserId,
        String accountId
) implements User {

    private static final String ACCOUNT_ID = "account_id";
    private static final String NAME = "name";

    public static CodeflixUser fromJwt(Jwt jwt, AccountFromUserIdResolver accountResolver) {
        final var idpUserId = jwt.getSubject();
        return new CodeflixUser(
                jwt.getClaimAsString(NAME),
                idpUserId,
                Optional.ofNullable(jwt.getClaimAsString(ACCOUNT_ID))
                        .or(() -> accountResolver.apply(new UserId(idpUserId)).map(acc -> acc.userId().value()))
                        .orElseThrow(() -> ForbiddenException.with("Could not resolve account from user"))
        );
    }
}
