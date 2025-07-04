package com.example.booking.config;

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
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1))
            .withStartupTimeout(Duration.ofMinutes(2))
            .withReuse(true);

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 1))
            .withStartupTimeout(Duration.ofMinutes(2))
            .withReuse(true);

    @Container
    protected static final KafkaContainer kafka = new KafkaContainer(KAFKA_IMAGE)
            .waitingFor(Wait.forLogMessage(".*\\[KafkaServer id=\\d+\\] started.*", 1))
            .withStartupTimeout(Duration.ofMinutes(2))
            .withReuse(true);


    static {
        // Ensure containers are fully started before anything else
        redis.start();
        postgres.start();
        kafka.start();

        System.out.println("✅ PostgreSQL: " + String.format(
                "r2dbc:postgresql://%s:%d/%s",
                postgres.getHost(),
                postgres.getMappedPort(5432),
                postgres.getDatabaseName()
        ));
        System.out.println("✅ Redis: " + redis.getHost() + ":" + redis.getMappedPort(6379));
        System.out.println("✅ Kafka: " + kafka.getBootstrapServers());
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        System.out.println("setting dynamic properties");
        registry.add("spring.r2dbc.url", () -> String.format(
            "r2dbc:postgresql://%s:%d/%s",
            postgres.getHost(),
            postgres.getMappedPort(5432),
            postgres.getDatabaseName()
        ));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", () -> redis.getMappedPort(6379));
    }
}
