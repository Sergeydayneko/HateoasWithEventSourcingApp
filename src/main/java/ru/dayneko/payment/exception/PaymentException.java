package ru.dayneko.payment.exception;

import lombok.Getter;
import ru.dayneko.order.model.Order;

@Getter
public class PaymentException extends RuntimeException {

	private final Order order;

	public PaymentException(Order order, String message) {

		super(message);

		this.order = order;
	}
}
