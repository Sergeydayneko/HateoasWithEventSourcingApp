package ru.dayneko.payment.service;

import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;
import ru.dayneko.payment.model.CreditCardPayment;
import ru.dayneko.payment.model.Payment;

import java.util.Optional;

/**
 * Interface to collect payment services.
 */
public interface PaymentService {

	/**
	 * Pay the given {@link Order} with the {@link CreditCard} identified by the given {@link CreditCardNumber}.
	 */
	CreditCardPayment pay(Order order, CreditCardNumber creditCardNumber);

	/**
	 * Returns the {@link Payment} for the given {@link Order}.
	 */
	Optional<Payment> getPaymentFor(Order order);

	/**
	 * Takes the receipt
	 */
	Optional<Payment.Receipt> takeReceiptFor(Order order);
}
