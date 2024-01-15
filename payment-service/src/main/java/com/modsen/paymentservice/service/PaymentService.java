package com.modsen.paymentservice.service;

import com.modsen.paymentservice.dto.request.CardRequest;
import com.modsen.paymentservice.dto.request.ChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerRequest;
import com.modsen.paymentservice.dto.response.BalanceCheckResponse;
import com.modsen.paymentservice.dto.response.BalanceResponse;
import com.modsen.paymentservice.dto.response.ChargeResponse;
import com.modsen.paymentservice.dto.response.CustomerResponse;
import com.modsen.paymentservice.dto.response.StringResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    StringResponse charge(ChargeRequest request) throws StripeException;

    StringResponse createToken(CardRequest request) throws StripeException;

    CustomerResponse createCustomer(CustomerRequest request) throws StripeException;

    CustomerResponse retrieveCustomer(Long id) throws StripeException;

    BalanceResponse getBalance() throws StripeException;

    ChargeResponse chargeFromCustomer(CustomerChargeRequest request) throws StripeException;

    BalanceCheckResponse checkCustomersBalance(CustomerChargeRequest request) throws StripeException;
}
