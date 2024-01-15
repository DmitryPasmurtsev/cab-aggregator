package com.modsen.driverservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(
                title = "Cab Aggregator for Modsen Company",
                version = "1.0.0",
                description = "Микросервис для работы с информацией о водителях",
                contact = @Contact(
                        name = "Dmitry Pasmurtsev",
                        email = "pasmurtsevd@gmail.com"
                )
        )
)
public class DriverServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }
}
