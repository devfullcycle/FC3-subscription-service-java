package com.fullcycle.codeflix.subscription.infrastructure.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Objects;

public class JdbcTemplateAdapter implements JdbcClient {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAdapter(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate);
    }

    public void update(final String sql, final Object... params) {
        this.jdbcTemplate.query(sql, params);
    }

    public void update(final String sql, final Object... params) {
        this.jdbcTemplate.update(sql, params);
    }
}
