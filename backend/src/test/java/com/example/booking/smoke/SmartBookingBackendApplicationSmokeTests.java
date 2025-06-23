package com.example.booking.smoke;

import com.example.booking.config.BaseTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class SmartBookingBackendApplicationSmokeTests extends BaseTestContainerConfig {

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
