package ru.dayneko.order.representation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import ru.dayneko.order.model.Order;

import java.time.LocalDateTime;

/**
 * Projection interface for REST Repository
 */
@Projection(
        name = "summaryOrder",
        types = Order.class
    )
public interface OrderProjection {

    LocalDateTime getOrderedDate();

    Order.Status getStatus();

    @Value("#{target.getLineItems().size()}")
    int numberOfOrderLineItems();
}
