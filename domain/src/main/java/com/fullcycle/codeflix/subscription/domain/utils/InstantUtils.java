package com.fullcycle.codeflix.subscription.domain.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtils {

    public static final int UNIX_PRECISION = 1_000;

    private InstantUtils() {
    }

    public static Instant now() {
        return Instant.now().truncatedTo(ChronoUnit.MILLIS);
    }

    public static Instant fromTimestamp(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }

        return new Timestamp(timestamp / UNIX_PRECISION).toInstant();
    }
}
