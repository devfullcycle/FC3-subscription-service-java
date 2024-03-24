package com.fullcycle.codeflix.subscription.domain.iam;

import com.fullcycle.codeflix.subscription.domain.AssertionConcern;

public record IAMUser(
        String id,
        String firstname,
        String lastname,
        String email,
        boolean enabled,
        boolean emailVerified,
        Attributes attributes
) implements AssertionConcern {

    private static final boolean ENABLED = true;
    private static final boolean EMAIL_VERIFIED = false;

    public IAMUser {
        this.assertArgumentLength(firstname, 2, "'firstname' should have at least 2 letters");
        this.assertArgumentLength(lastname, 2, "'lastname' should have at least 2 letters");
        this.assertArgumentNotEmpty(email, "'email' should not be empty");
    }

    public static IAMUser newUser(
            final String firstname,
            final String lastname,
            final String email,
            final String password
    ) {
        final var attributes = new Attributes();
        attributes.put("password", password);

        return new IAMUser(null, firstname, lastname, email, ENABLED, EMAIL_VERIFIED, attributes);
    }
}
