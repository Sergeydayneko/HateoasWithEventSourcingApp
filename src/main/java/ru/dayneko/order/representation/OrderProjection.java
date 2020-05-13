package ru.dayneko.order.representation;

import org.springframework.data.rest.core.config.Projection;
import ru.dayneko.order.model.Order;

import java.time.LocalDateTime;

/**
 * Projection interface for REST Repository
 */
@Projection(name = "summary", types = Order.class)
public interface OrderProjection {

    LocalDateTime getOrderedDate();

    Order.Status getStatus();
}
