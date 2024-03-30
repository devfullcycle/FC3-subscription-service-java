package com.fullcycle.codeflix.subscription.infrastructure.database;

import java.util.List;
import java.util.Map;

public interface DatabaseClient {

    <T> List<T> query(final String sql, final RowMapping<T> clzz);

    <T> List<T> query(final String sql, final Map<String, Object> params, final RowMapping<T> map);

    int update(final String sql, final Map<String, Object> params);
}
