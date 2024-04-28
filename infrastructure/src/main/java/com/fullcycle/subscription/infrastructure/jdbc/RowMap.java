package com.fullcycle.subscription.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMap<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
