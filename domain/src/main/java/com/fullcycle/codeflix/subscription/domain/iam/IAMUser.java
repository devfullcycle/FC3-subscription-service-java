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

}
