package ru.dayneko.payment.representation;

import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.mediatype.hal.HalLinkRelation;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.TypedEntityLinks;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.Payment;

import javax.validation.constraints.NotNull;

import static ru.dayneko.config.Constants.CURIE_NAMESPACE;

/**
 * Helper component to create links to the {@link Payment} and {@link Payment.Receipt}.
 */
@Component
public class PaymentLinks {

	static final String PAYMENT = "/payment";
	static final String RECEIPT = "/receipt";
	private static final LinkRelation PAYMENT_REL = HalLinkRelation.curied(CURIE_NAMESPACE, "payment");
	private static final LinkRelation RECEIPT_REL = HalLinkRelation.curied(CURIE_NAMESPACE, "receipt");

	@Getter
	private final TypedEntityLinks<Order> orderLinks;

	/**
	 * Creates a new {@link PaymentLinks} for the given {@link EntityLinks}.
	 */
	PaymentLinks(@NotNull EntityLinks entityLinks) {
		this.orderLinks = entityLinks.forType(Order::getId);
	}

	/**
	 * Returns the {@link Link} to point to the {@link Payment} for the given {@link Order}.
	 */
	public Link getPaymentLink(@NotNull Order order) {
		return orderLinks.linkForItemResource(order).slash(PAYMENT).withRel(PAYMENT_REL);
	}

	/**
	 * Returns the {@link Link} to the {@link Payment.Receipt} of the given {@link Order}.
	 */
	public Link getReceiptLink(@NotNull Order order) {
		return orderLinks.linkForItemResource(order).slash(RECEIPT).withRel(RECEIPT_REL);
	}
}
