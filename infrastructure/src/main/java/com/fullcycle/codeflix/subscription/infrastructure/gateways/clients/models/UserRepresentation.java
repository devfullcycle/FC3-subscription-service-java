package com.fullcycle.codeflix.subscription.infrastructure.gateways.clients.models;

import java.util.HashMap;

public record UserRepresentation(
        String id,
        String firstname,
        String lastname,
        String email,
        boolean enabled,
        boolean emailVerified,
        Attributes attributes,
        Credentials credentials
) {

    public static class Attributes extends HashMap<String, Object> {

    }

    public record Credentials(String type, String value, boolean temporary) {
    }
}
