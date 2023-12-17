package com.modsen.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @NotNull(message = "Passenger id can't be empty")
    Long passengerId;
    @NotBlank(message = "Card number can't be empty")
    String cardNumber;
    @NotNull(message = "Expiration month can't be empty")
    int expMonth;
    @NotNull(message = "Expiration year can't be empty")
    int expYear;
    @NotBlank(message = "Cvc can't be empty")
    @Length(max = 3,message = "Max length is 3")
    String cvc;
}
