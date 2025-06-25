package com.example.booking.config;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Testcontainers
public abstract class BaseTestContainerConfig {

    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15");
    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7.2");
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.6.0");

    @Container
    protected static final GenericContainer<?> redis = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)))
            .withReuse(false);

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)))
            .withReuse(false);

    @Container
    protected static final KafkaContainer kafka = new KafkaContainer(KAFKA_IMAGE)
            .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)))
            .withReuse(false);



    @BeforeAll
    static void startContainers() {
        System.out.println("✅ Starting test containers...");
        // Ensure containers are fully started before anything else
        redis.start();
        postgres.start();
        kafka.start();
        try {
            Thread.sleep(2000); // Let ports settle
        } catch (InterruptedException ignored) {}


        System.out.println("✅ PostgreSQL: " + postgres.getJdbcUrl());
        System.out.println("✅ Redis: " + redis.getHost() + ":" + redis.getMappedPort(6379));
        System.out.println("✅ Kafka: " + kafka.getBootstrapServers());
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        System.out.println("setting dynamic properties");
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
        System.out.println("completed dynamic properties");
    }
}
