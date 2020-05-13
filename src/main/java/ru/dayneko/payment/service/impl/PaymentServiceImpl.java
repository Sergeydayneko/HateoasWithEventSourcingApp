package ru.dayneko.payment.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dayneko.order.model.Order;
import ru.dayneko.order.repository.OrderRepository;
import ru.dayneko.payment.exception.PaymentException;
import ru.dayneko.payment.model.CreditCardNumber;
import ru.dayneko.payment.model.CreditCardPayment;
import ru.dayneko.payment.model.Payment;
import ru.dayneko.payment.repository.CreditCardRepository;
import ru.dayneko.payment.repository.PaymentRepository;
import ru.dayneko.payment.service.PaymentService;

import java.util.Optional;

/**
 * Implementation of {@link PaymentService} delegating persistence operations to {@link PaymentRepository} and
 * {@link CreditCardRepository}.
 * @author Stéphane Nicoll
 */
@Service
@Transactional
@RequiredArgsConstructor
class PaymentServiceImpl implements PaymentService {

	@NonNull
	private final CreditCardRepository creditCardRepository;

	@NonNull
	private final PaymentRepository paymentRepository;

	@NonNull
	private final OrderRepository orderRepository;

	@Override
	public CreditCardPayment pay(Order order, CreditCardNumber creditCardNumber) {

		if (order.isPaid()) {
			throw new PaymentException(order, "Order already paid!");
		}

		var creditCardResult = creditCardRepository.findByNumber(creditCardNumber);

		if (creditCardResult.isEmpty()) {
			throw new PaymentException(order,
					String.format("No credit card found for number: %s", creditCardNumber.getNumber()));
		}

		var creditCard = creditCardResult.get();

		if (!creditCard.isValid()) {
			throw new PaymentException(order, String.format("Invalid credit card with number %s, expired %s!",
					creditCardNumber.getNumber(), creditCard.getExpirationDate()));
		}

		var payment = paymentRepository.save(new CreditCardPayment(creditCard, order));

		orderRepository.save(order.markPaid());

		return payment;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Payment> getPaymentFor(Order order) {
		return paymentRepository.findByOrder(order);
	}

	@Override
	public Optional<Payment.Receipt> takeReceiptFor(Order order) {

		var result = orderRepository.save(order.markTaken());

		return getPaymentFor(result).map(Payment::getReceipt);
	}
}
