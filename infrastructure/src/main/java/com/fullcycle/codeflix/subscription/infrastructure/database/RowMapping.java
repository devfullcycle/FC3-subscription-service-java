package com.fullcycle.codeflix.subscription.infrastructure.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapping<T> {
    T mapRow(ResultSet rs) throws SQLException;
}
