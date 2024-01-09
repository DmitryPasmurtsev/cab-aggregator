package com.modsen.rideservice.feign.client;

import com.modsen.rideservice.feign.dto.CustomerChargeRequest;
import com.modsen.rideservice.feign.dto.CustomerRequest;
import com.modsen.rideservice.feign.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        value = "payments",
        url = "http://localhost:9004/api/v1/payments"
)
public interface PaymentClient {

    @GetMapping("/customers/{id}")
    CustomerResponse findCustomer(@PathVariable long id);

    @PostMapping("/customers/charge")
    void chargeFromCustomer(@RequestBody CustomerChargeRequest request);

    @PostMapping("/customers")
    CustomerResponse createCustomer(@RequestBody CustomerRequest request);

    @PostMapping("/customers/checkBalance")
    void checkCustomersBalance(@RequestBody CustomerChargeRequest request);
}
