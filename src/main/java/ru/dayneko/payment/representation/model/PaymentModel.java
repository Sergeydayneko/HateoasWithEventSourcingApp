package ru.dayneko.payment.representation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ru.dayneko.payment.model.CreditCard;

import javax.money.MonetaryAmount;

@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class PaymentModel extends RepresentationModel<PaymentModel> {

    private final MonetaryAmount amount;
    private final CreditCard creditCard;
}

