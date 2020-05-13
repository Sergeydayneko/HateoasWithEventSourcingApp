package ru.dayneko.order.representation;

import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;
import ru.dayneko.order.exception.OrderAlreadyPaid;
import ru.dayneko.order.model.Order;

/**
 * Event listener to reject {@code DELETE} requests to Spring Data REST.
 */
@Component
class OrderControllerEventListener extends AbstractRepositoryEventListener<Order> {

	@Override
	protected void onBeforeDelete(Order order) {
		if (order.isPaid()) {
			throw new OrderAlreadyPaid();
		}
	}
}
