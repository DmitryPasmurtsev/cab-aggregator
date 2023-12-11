package com.modsen.passengerservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Cab Aggregator for Modsen Company",
                version = "1.0.0",
                description = "Микросервис для работы с информацией о пассажирах",
                contact = @Contact(
                        name = "Dmitry Pasmurtsev",
                        email = "pasmurtsevd@gmail.com"
                )
        )
)
public class PassengerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassengerServiceApplication.class, args);
    }
}
