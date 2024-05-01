package com.fullcycle.subscription.infrastructure.utils;

import java.net.URI;

public abstract class KeycloakUtils {
    private KeycloakUtils() {}

    public static String extractIdFromLocation(final URI location) {
        if (location == null) {
            return null;
        }
        var path = location.getPath().split("/");
        return path[path.length - 1];
    }
}
