package com.modsen.paymentservice.controller;

import com.modsen.paymentservice.dto.request.CardRequest;
import com.modsen.paymentservice.dto.request.ChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerRequest;
import com.modsen.paymentservice.dto.response.BalanceResponse;
import com.modsen.paymentservice.dto.response.CustomerResponse;
import com.modsen.paymentservice.dto.response.StringResponse;
import com.modsen.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public StringResponse chargeCard(@RequestBody @Valid ChargeRequest chargeRequest) throws StripeException {
        return paymentService.charge(chargeRequest);
    }

    @PostMapping("/token")
    public StringResponse createToken(@RequestBody @Valid CardRequest request) throws StripeException {
        return paymentService.createToken(request);
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody @Valid CustomerRequest request) throws StripeException {
        return paymentService.createCustomer(request);
    }

    @GetMapping("/customers/{id}")
    public CustomerResponse findCustomer(@PathVariable Long id) throws StripeException {
        return paymentService.retrieveCustomer(id);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance() throws StripeException {
        return paymentService.getBalance();
    }

    @PostMapping("/customers/checkBalance")
    public void checkBalance(@RequestBody CustomerChargeRequest dto) throws StripeException {
        paymentService.checkCustomersBalance(dto);
    }

    @PostMapping("/customers/charge")
    public void chargeFromCustomer(@RequestBody @Valid CustomerChargeRequest request) throws StripeException {
        paymentService.chargeFromCustomer(request);
    }
}
