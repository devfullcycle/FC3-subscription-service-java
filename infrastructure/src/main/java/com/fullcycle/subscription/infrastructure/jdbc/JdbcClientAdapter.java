package com.fullcycle.subscription.infrastructure.jdbc;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class JdbcClientAdapter implements DatabaseClient {

    private final JdbcClient target;

    public JdbcClientAdapter(final JdbcClient target) {
        this.target = Objects.requireNonNull(target);
    }

    @Override
    public <T> Optional<T> queryOne(String sql, Map<String, Object> params, RowMap<T> mapper) {
        return this.target.sql(sql).params(params).query(new RowMapAdapter<>(mapper)).optional();
    }

    @Override
    public <T> List<T> query(String sql, RowMap<T> mapper) {
        return this.target.sql(sql).query(new RowMapAdapter<>(mapper)).list();
    }

    @Override
    public <T> List<T> query(String sql, Map<String, Object> params, RowMap<T> mapper) {
        return this.target.sql(sql).params(params).query(new RowMapAdapter<>(mapper)).list();
    }

    @Override
    public int update(String sql, Map<String, Object> params) {
        try {
            return this.target.sql(sql).params(params).update();
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }
}
