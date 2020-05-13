package ru.dayneko.payment;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.dayneko.order.model.Order;

/**
 * Event to be thrown when an {@link Order} has been payed.
 */
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class OrderPaid {

	private final long orderId;

}
