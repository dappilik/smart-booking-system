package com.example.booking.smoke;

import com.example.booking.config.BaseTestContainerConfig;
import com.example.booking.config.RedisTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = com.example.booking.SmartBookingBackendApplication.class)
@Import({RedisTestConfig.class})
public class SmartBookingBackendApplicationSmokeTest extends BaseTestContainerConfig {

	@Test
	void contextLoads() {
	}

	@Test
	void postgresConnection() {
		assertTrue(postgres.isRunning());
	}

	@Test
	void kafkaIsUp() {
		assertTrue(kafka.isRunning());
	}

	@Test
	void redisIsUp() {
		assertTrue(redis.isRunning());
	}

}
