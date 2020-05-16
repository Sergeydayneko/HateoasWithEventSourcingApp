package ru.dayneko.payment.representation.processor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.representation.PaymentController;
import ru.dayneko.payment.representation.PaymentLinks;

/**
 * {@link ResourceProcessor} to enrich {@link Order} {@link ConfigurationSource.Resource}s with links to the {@link PaymentController}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class PaymentOrderModelProcessor implements RepresentationModelProcessor<EntityModel<Order>> {

	@NonNull
	private final PaymentLinks paymentLinks;

	@Override
	public EntityModel<Order> process(EntityModel<Order> resource) {

		var order = resource.getContent();

		if (order != null ) {
			if (!order.isPaid()) {
				log.info("Order has been enriched by payment links");

				resource.add(paymentLinks.getPaymentLink(order));
			}

			if (order.isReady()) {
				log.info("Order has been enriched by receipt links");

				resource.add(paymentLinks.getReceiptLink(order));
			}
		}

		return resource;
	}
}
