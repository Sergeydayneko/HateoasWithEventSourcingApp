package ru.dayneko.payment.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.dayneko.order.model.Order;

/**
 * Event to be thrown when an {@link Order} has been payed.
 */
@Getter
@EqualsAndHashCode
@ToString
public class OrderPaid {

	private final long orderId;

	/**
	 * Creates a new {@link OrderPaid}
	 * 
	 * @param orderId the id of the order that just has been payed
	 */
	public OrderPaid(long orderId) {
		this.orderId = orderId;
	}
}
