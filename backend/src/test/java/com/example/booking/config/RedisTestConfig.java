package com.example.booking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@TestConfiguration
public class RedisTestConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(
            @Value("${spring.redis.host}") String host,
            @Value("${spring.redis.port}") int port) {
        System.out.println("⚙️ Manually configuring RedisConnectionFactory with: " + host + ":" + port);
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }
}