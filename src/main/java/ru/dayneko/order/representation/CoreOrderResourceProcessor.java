package ru.dayneko.order.representation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import ru.dayneko.order.model.Order;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * {@link ResourceProcessor} implementation to add links to the {@link Order} representation that indicate that the
 * Order can be updated or cancelled as long as it has not been paid yet.
 */
@Component
@RequiredArgsConstructor
class CoreOrderResourceProcessor implements RepresentationModelProcessor<EntityModel<Order>> {

	public static final String CANCEL_REL = "cancel";
	public static final String UPDATE_REL = "update";

	private final @NonNull EntityLinks entityLinks;

	@Override
	public EntityModel<Order> process(@NotNull EntityModel<Order> resource) {

		var typedEntityLinks = entityLinks.forType(Order::getId);
		var order = resource.getContent();

		if (Objects.nonNull(order) && !order.isPaid()) {
			
			var orderLink = typedEntityLinks.linkForItemResource(order);
			
			resource.add(orderLink.withRel(CANCEL_REL));
			resource.add(orderLink.withRel(UPDATE_REL));
		}

		return resource;
	}
}
