package com.fullcycle.codeflix.subscription.infrastructure.database;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperAdapter<T> implements RowMapper<T> {

    private final RowMapping<T> delegate;

    public RowMapperAdapter(final RowMapping<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T mapRow(final ResultSet rs, int rowNum) throws SQLException {
        return this.delegate.mapRow(rs);
    }
}
