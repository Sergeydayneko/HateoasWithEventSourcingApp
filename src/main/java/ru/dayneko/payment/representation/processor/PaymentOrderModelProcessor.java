package ru.dayneko.payment.representation.processor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.representation.PaymentLinks;

/**
 * {@link ResourceProcessor} to enrich {@link Order} {@link ConfigurationSource.Resource}s with links to the {@link PaymentController}.
 */
@Component
@RequiredArgsConstructor
class PaymentOrderModelProcessor implements RepresentationModelProcessor<EntityModel<Order>> {

	private final @NonNull PaymentLinks paymentLinks;


	@Override
	public EntityModel<Order> process(EntityModel<Order> resource) {

		var order = resource.getContent();

		if (!order.isPaid()) {
			resource.add(paymentLinks.getPaymentLink(order));
		}

		if (order.isReady()) {
			resource.add(paymentLinks.getReceiptLink(order));
		}

		return resource;
	}
}
