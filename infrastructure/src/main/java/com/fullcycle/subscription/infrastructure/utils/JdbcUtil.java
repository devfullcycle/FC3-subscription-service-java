package com.fullcycle.subscription.infrastructure.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

public abstract class JdbcUtil {

    private JdbcUtil() {}

    public static Instant getInstant(ResultSet rs, String prop) throws SQLException {
        if (prop == null || rs == null) {
            return null;
        }
        final var ts = rs.getTimestamp(prop);
        if (ts == null) {
            return null;
        }
        return ts.toInstant();
    }

    public static LocalDate getLocalDate(ResultSet rs, String prop) throws SQLException {
        if (prop == null || rs == null) {
            return null;
        }
        final var ts = rs.getDate(prop);
        if (ts == null) {
            return null;
        }
        return ts.toLocalDate();
    }
}
