package ru.dayneko.payment.service;

import ru.dayneko.order.model.Order;
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
	 * 
	 * @param order
	 * @param creditCardNumber
	 * @return
	 */
	CreditCardPayment pay(Order order, CreditCardNumber creditCardNumber);

	/**
	 * Returns the {@link Payment} for the given {@link Order}.
	 * 
	 * @param order
	 * @return the {@link Payment} for the given {@link Order} or {@link Optional#empty()} if the Order hasn't been payed
	 *         yet.
	 */
	Optional<Payment> getPaymentFor(Order order);

	/**
	 * Takes the receipt
	 * 
	 * @param order
	 * @return
	 */
	Optional<Payment.Receipt> takeReceiptFor(Order order);
}
