package ru.dayneko.payment.representation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.Payment;
import ru.dayneko.payment.representation.model.PaymentForm;
import ru.dayneko.payment.representation.model.PaymentModel;
import ru.dayneko.payment.service.PaymentService;

import javax.validation.Valid;

/**
 * Controller for payments for an {@link Order}.
 */
@RestController
@RequestMapping("/orders/{id}")
@ExposesResourceFor(Payment.class) // Hateoas
@RequiredArgsConstructor
public class PaymentController {

	@NonNull
	private final PaymentService paymentService;

	@NonNull
	private final PaymentLinks paymentLinks;

	/**
	 * Accepts a payment for an {@link Order}
	 */
	@PutMapping(path = PaymentLinks.PAYMENT)
	ResponseEntity<PaymentModel> submitPayment(@PathVariable("id") Order order, @Valid @RequestBody PaymentForm ccn) {

		if (order == null || order.isPaid()) {
			return ResponseEntity.notFound().build();
		}

		var payment = paymentService.pay(order, ccn.getNumber());

		var paymentModel = new PaymentModel(order.getPrice(), payment.getCreditCard())
								.add(paymentLinks.getOrderLinks().linkToItemResource(order)); // add link for order

		/* Location header for created resource */
		var paymentUri = paymentLinks.getPaymentLink(order).toUri();

		return ResponseEntity.created(paymentUri).body(paymentModel);
	}

	/**
	 * Shows the {@link Payment.Receipt} for the given order.
	 */
	@GetMapping(path = PaymentLinks.RECEIPT)
	HttpEntity<EntityModel<Payment.Receipt>> showReceipt(@PathVariable("id") Order order) {

		if (order == null || !order.isPaid() || order.isTaken()) {
			return ResponseEntity.notFound().build();
		}

		return paymentService.getPaymentFor(order)
				.map(Payment::getReceipt)
				.map(this::createReceiptResponse)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Takes the {@link Payment.Receipt} for the given {@link Order} and thus completes the process.
	 */
	@DeleteMapping(path = PaymentLinks.RECEIPT)
	HttpEntity<EntityModel<Payment.Receipt>> takeReceipt(@PathVariable("id") Order order) {

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
}
