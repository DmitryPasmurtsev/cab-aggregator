package com.modsen.passengerservice.integration.kafka;

import com.modsen.passengerservice.dto.request.RatingUpdateDto;
import com.modsen.passengerservice.entity.Passenger;
import com.modsen.passengerservice.integration.BaseIntegrationTest;
import com.modsen.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static com.modsen.passengerservice.util.TestUtils.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import(ProducerConfig.class)
public class RatingConsumerTest extends BaseIntegrationTest {

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @DynamicPropertySource
    static void initKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    private final PassengerRepository passengerRepository;
    private final KafkaTemplate<String, RatingUpdateDto> kafkaTemplate;

    @BeforeAll
    public static void beforeAll() {
        kafka.start();
        postgres.start();
    }

    @AfterAll
    public static void afterAll() {
        kafka.start();
        postgres.start();
    }

    @Test
    void updatePassengerRating_shouldUpdatePassengerRating_whenMessageConsumed() {
        passengerRepository.save(getDefaultPassenger());

        kafkaTemplate.send("update-passenger-rating", getRatingUpdateDto());

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(60, SECONDS)
                .untilAsserted(() -> {
                    Passenger passenger = passengerRepository.findById(DEFAULT_ID).get();
                    Assertions.assertEquals(NEW_RATING, passenger.getRating());
                });
    }
}
