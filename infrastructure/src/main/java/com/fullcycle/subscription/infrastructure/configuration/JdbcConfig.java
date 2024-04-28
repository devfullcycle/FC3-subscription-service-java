package com.fullcycle.subscription.infrastructure.configuration;

import com.fullcycle.subscription.infrastructure.jdbc.DatabaseClient;
import com.fullcycle.subscription.infrastructure.jdbc.JdbcClientAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration(proxyBeanMethods = false)
public class JdbcConfig {

    @Bean
    DatabaseClient databaseClient(JdbcClient jdbcClient) {
        return new JdbcClientAdapter(jdbcClient);
    }
}
