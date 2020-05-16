package ru.dayneko.payment.service;

import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.CreditCardNumber;
import ru.dayneko.payment.model.CreditCardPayment;
import ru.dayneko.payment.model.Payment;

import java.util.Optional;

public interface PaymentService {

	CreditCardPayment pay(Order order, CreditCardNumber creditCardNumber);


	Optional<Payment> getPaymentFor(Order order);


	Optional<Payment.Receipt> takeReceiptFor(Order order);
}
