package com.fullcycle.subscription.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

public final class JdbcUtils {

    private JdbcUtils() {}

    public static Instant getInstant(final ResultSet rs, final String prop) throws SQLException {
        if (prop == null || prop.isBlank() || rs == null) {
            return null;
        }
        var ts = rs.getTimestamp(prop);
        if (ts == null) {
            return null;
        }
        return ts.toInstant();
    }

    public static LocalDate getLocalDate(final ResultSet rs, final String prop) throws SQLException {
        if (prop == null || prop.isBlank() || rs == null) {
            return null;
        }
        var ts = rs.getDate(prop);
        if (ts == null) {
            return null;
        }
        return ts.toLocalDate();
    }
}
