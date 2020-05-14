package ru.dayneko.payment.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;
import ru.dayneko.payment.model.Payment;
import ru.dayneko.payment.service.PaymentService;
import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Spring MVC controller to handle payments for an {@link Order}.
 */
@Controller
@RequestMapping("/orders/{id}")
@ExposesResourceFor(Payment.class)
@RequiredArgsConstructor
class PaymentController {

	@NonNull
	private final PaymentService paymentService;

	@NonNull
	private final PaymentLinks paymentLinks;

	/**
	 * Accepts a payment for an {@link Order}
	 */
	@PutMapping(path = PaymentLinks.PAYMENT)
	ResponseEntity<?> submitPayment(@PathVariable("id") Order order, @Valid @RequestBody PaymentForm ccn) {

		if (order == null || order.isPaid()) {
			return ResponseEntity.notFound().build();
		}

		var payment = paymentService.pay(order, ccn.getNumber());

		var paymentModel = new PaymentModel(order.getPrice(), payment.getCreditCard())
								.add(paymentLinks.getOrderLinks().linkToItemResource(order));

		var paymentUri = paymentLinks.getPaymentLink(order).toUri();

		return ResponseEntity.created(paymentUri).body(paymentModel);
	}

	/**
	 * Shows the {@link Payment.Receipt} for the given order.
	 */
	@GetMapping(path = PaymentLinks.RECEIPT)
	HttpEntity<?> showReceipt(@PathVariable("id") Order order) {

		if (order == null || !order.isPaid() || order.isTaken()) {
			return ResponseEntity.notFound().build();
		}

		return paymentService.getPaymentFor(order) //
				.map(Payment::getReceipt) //
				.map(this::createReceiptResponse) //
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Takes the {@link Payment.Receipt} for the given {@link Order} and thus completes the process.
	 */
	@DeleteMapping(path = PaymentLinks.RECEIPT)
	HttpEntity<?> takeReceipt(@PathVariable("id") Order order) {

		if (order == null || !order.isPaid()) {
			return ResponseEntity.notFound().build();
		}

		return paymentService.takeReceiptFor(order)
				.map(this::createReceiptResponse)
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED));
	}

	/**
	 * Renders the given {@link Payment.Receipt} including links to the associated {@link Order} as well as a self link in case
	 * the {@link Payment.Receipt} is still available.
	 */
	private HttpEntity<EntityModel<Payment.Receipt>> createReceiptResponse(Payment.Receipt receipt) {

		var orderLinks = paymentLinks.getOrderLinks();
		var order = receipt.getOrder();

		var model = new EntityModel<>(receipt)
				.add(orderLinks.linkToItemResource(order));

		if (!order.isTaken()) {
			model.add(orderLinks.linkForItemResource(order).slash("receipt").withSelfRel());
		}

		return ResponseEntity.ok(model);
	}

	/**
	 * EntityModel implementation for payment results.
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	@RequiredArgsConstructor
	static class PaymentModel extends RepresentationModel<PaymentModel> {

		private final MonetaryAmount amount;
		private final CreditCard creditCard;
	}

	@Value
	@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
	static class PaymentForm {

		@NotNull
		CreditCardNumber number;
	}
}
