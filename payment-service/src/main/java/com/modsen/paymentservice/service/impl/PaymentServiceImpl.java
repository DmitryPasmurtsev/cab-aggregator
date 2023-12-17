package com.modsen.paymentservice.service.impl;

import com.modsen.paymentservice.dto.request.CardRequest;
import com.modsen.paymentservice.dto.request.ChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerChargeRequest;
import com.modsen.paymentservice.dto.request.CustomerRequest;
import com.modsen.paymentservice.dto.response.*;
import com.modsen.paymentservice.entity.User;
import com.modsen.paymentservice.exceptions.NotCreatedException;
import com.modsen.paymentservice.exceptions.NotFoundException;
import com.modsen.paymentservice.repository.CustomerRepository;
import com.modsen.paymentservice.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentSourceCollectionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.key.secret}")
    private String SECRET_KEY;
    @Value("${stripe.key.public}")
    private String PUBLIC_KEY;
    private final CustomerRepository customerRepository;

    @Override
    public StringResponse charge(ChargeRequest request) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", request.getAmount() * 100);
        params.put("currency", "usd");
        params.put("source", request.getCardToken());
        Charge charge = Charge.create(params);
        String message = "Payment successful. ID: " + charge.getId();
        return new StringResponse(message);
    }

    public StringResponse createToken(CardRequest request) throws StripeException {
        Stripe.apiKey = PUBLIC_KEY;
        Map<String, Object> card = new HashMap<>();
        card.put("number", request.getCardNumber());
        card.put("exp_month", request.getExpMonth());
        card.put("exp_year", request.getExpYear());
        card.put("cvc", request.getCvc());
        Map<String, Object> params = new HashMap<>();
        params.put("card", card);
        Token token = Token.create(params);
        return new StringResponse(token.getId());
    }

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) throws StripeException {
        Stripe.apiKey = PUBLIC_KEY;
        if (customerRepository.existsById(request.getPassengerId()))
            throw new NotCreatedException("id", "Customer with id " + request.getPassengerId() + " already exists");
        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(request.getName())
                        .setEmail(request.getEmail())
                        .setPhone(request.getPhone())
                        .setBalance(Math.round(request.getBalance() * 100))
                        .build();
        Stripe.apiKey = SECRET_KEY;
        return createUser(params, request.getPassengerId());
    }

    private CustomerResponse createUser(CustomerCreateParams params, long id) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        Customer customer = Customer.create(params);
        createPaymentMethod(customer.getId());
        User user = User
                .builder()
                .customerId(customer.getId())
                .passengerId(id).build();
        customerRepository.save(user);
        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName())
                .build();
    }

    private void createPaymentMethod(String customerId) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("type", "card");
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("token", "tok_visa"); // здесь "tok_visa" - это пример токена тестирования для карты Visa
        paymentMethodParams.put("card", cardParams);
        PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);
        Map<String, Object> attachParams = new HashMap<>();
        attachParams.put("customer", customerId);
        paymentMethod.attach(attachParams);
    }

    public CustomerResponse retrieveCustomer(Long id) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        User user = getEntityById(id);
        Customer customer = Customer.retrieve(user.getCustomerId());

        return CustomerResponse.builder()
                .id(customer.getId())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .name(customer.getName()).build();
    }

    public BalanceResponse getBalance() throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        Balance balance = Balance.retrieve();
        return BalanceResponse
                .builder()
                .amount(balance.getPending().get(0).getAmount()/100.0)
                .currency(balance.getPending().get(0).getCurrency())
                .build();
    }

    public ChargeResponse chargeFromCustomer(CustomerChargeRequest request) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        User user = getEntityById(request.getPassengerId());
        String customerId = user.getCustomerId();
        Map<String, Object> paymentIntentParams = new HashMap<>();
        paymentIntentParams.put("amount", request.getAmount() * 100);
        paymentIntentParams.put("currency", "byn");
        paymentIntentParams.put("customer", customerId);
        PaymentIntent intent = PaymentIntent.create(paymentIntentParams);
        intent.setPaymentMethod(customerId);
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod("pm_card_visa")
                        .build();
        intent.confirm(params);
        return ChargeResponse.builder()
                .id(intent.getId())
                .amount(intent.getAmount()/100.0)
                .currency(intent.getCurrency()).build();
    }

    public void createCard(CardRequest dto) throws StripeException {
        Stripe.apiKey = SECRET_KEY;
        User user = getEntityById(dto.getPassengerId());
        Customer customer = Customer.retrieve(user.getCustomerId());
        PaymentSourceCollectionCreateParams params =
                PaymentSourceCollectionCreateParams.builder().setSource("tok_visa").build();
        //PaymentSource paymentSource = customer.getPaymentSources().create(params);
    }

    private User getEntityById(Long id) {
        Optional<User> user = customerRepository.findById(id);
        if(user.isPresent()) return user.get();
        throw new NotFoundException("passengerId", "Customer with id={"+id+"} not found");
    }

}