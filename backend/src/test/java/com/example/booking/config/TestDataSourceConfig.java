package com.example.booking.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;

@TestConfiguration
public class TestDataSourceConfig {

    @Bean
    public DataSource dataSource(BaseTestContainerConfig config) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(config.getPostgresJdbcUrl());
        ds.setUsername(config.getPostgresUsername());
        ds.setPassword(config.getPostgresPassword());
        ds.setMaximumPoolSize(5);
        ds.setConnectionTimeout(10000);
        ds.setMaxLifetime(10000);  // Avoid premature timeout
        return ds;
    }
}
