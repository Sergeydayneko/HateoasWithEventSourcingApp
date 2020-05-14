package ru.dayneko;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javamoney.moneta.Money;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.dayneko.order.model.LineItem;
import ru.dayneko.order.model.Order;
import ru.dayneko.order.repository.OrderRepository;

import java.util.List;

import static ru.dayneko.config.Currencies.EURO;

/**
 * Initializer to set up two {@link Order}s.
 */
@Service
@RequiredArgsConstructor
class OrderInitializer {

	@NonNull
	private final OrderRepository orders;

	/**
	 * Creates two orders and persists them using the given {@link OrderRepository}.
	 * 
	 * @param orders must not be {@literal null}.
	 */
	@EventListener
	public void init(ApplicationReadyEvent event) {

		if (orders.count() != 0) {
			return;
		}

		var javaChip = new LineItem("Java Chip", Money.of(4.20, EURO));
		var cappuchino = new LineItem("Cappuchino", Money.of(3.20, EURO));

		orders.saveAll(List.of(new Order(javaChip), new Order(cappuchino)));
	}
}
