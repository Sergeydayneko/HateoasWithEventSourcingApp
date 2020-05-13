package ru.dayneko.payment.model;

import lombok.Getter;
import ru.dayneko.order.model.Order;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * A {@link Payment} done through a {@link CreditCard}.
 */
@Entity
@Getter
public class CreditCardPayment extends Payment {

	@ManyToOne
	private final CreditCard creditCard;

	protected CreditCardPayment() {
		this.creditCard = null;
	}

	/**
	 * Creates a new {@link CreditCardPayment} for the given {@link CreditCard} and {@link Order}.
	 */
	public CreditCardPayment(@NotNull CreditCard creditCard, Order order) {

		super(order);

		this.creditCard = creditCard;
	}
}
