package com.fullcycle.subscription.infrastructure.jdbc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class RowMapAdapter<T> implements RowMapper<T> {

    private final RowMap<T> target;

    public RowMapAdapter(final RowMap<T> target) {
        this.target = Objects.requireNonNull(target);
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        return this.target.mapRow(rs);
    }
}
