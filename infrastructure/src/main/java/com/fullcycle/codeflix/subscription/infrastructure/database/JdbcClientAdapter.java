package com.fullcycle.codeflix.subscription.infrastructure.database;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class JdbcClientAdapter implements DatabaseClient {

    private final JdbcClient jdbcClient;

    public JdbcClientAdapter(final JdbcClient jdbcClient) {
        this.jdbcClient = Objects.requireNonNull(jdbcClient);
    }

    @Override
    public int update(final String sql, final Map<String, Object> params) {
        return this.jdbcClient.sql(sql).params(params).update();
    }

    @Override
    public <T> List<T> query(final String sql, final RowMapping<T> map) {
        return query(sql, Map.of(), map);
    }

    @Override
    public <T> List<T> query(final String sql, final Map<String, Object> params, final RowMapping<T> map) {
        return this.jdbcClient.sql(sql)
                .params(params)
                .query(new RowMapperAdapter<T>(map))
                .list();
    }
}
