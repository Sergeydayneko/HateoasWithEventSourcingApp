package ru.dayneko.payment.representation.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dayneko.payment.model.CreditCardNumber;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
public class PaymentForm {

    @NotNull
    private final CreditCardNumber number;
}